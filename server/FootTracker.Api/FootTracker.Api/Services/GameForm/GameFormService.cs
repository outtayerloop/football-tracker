using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Domain.Models;
using FootTracker.Api.Repositories;
using FootTracker.Api.Services.CreationChecker;
using FootTracker.Api.Utils;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FootTracker.Api.Services
{
    public class GameFormService : IGameFormService
    {

        private readonly IChampionshipRepository _championshipRepository;
        private readonly IRefereeRepository _refereeRepository;
        private readonly IMembershipRepository _membershipRepository;
        private readonly IPlayerRepository _playerRepository;
        private readonly IGameRepository _gameRepository;

        private readonly ICreationCheckerService _creationCheckerService;

        public GameFormService(IChampionshipRepository championshipRepository, 
            IRefereeRepository refereeRepository, IMembershipRepository membershipRepository, 
            IPlayerRepository playerRepository, IGameRepository gameRepository, 
            ICreationCheckerService creationCheckerService)
        {
            _championshipRepository = championshipRepository;
            _refereeRepository = refereeRepository;
            _membershipRepository = membershipRepository;
            _playerRepository = playerRepository;
            _gameRepository = gameRepository;
            _creationCheckerService = creationCheckerService;
        }

        public List<ChampionshipDto> GetAllChampionships()
        {
            List<Championship> championships = _championshipRepository.GetAll();
            return ConversionUtil.GetAllDtosFromModels<Championship, ChampionshipDto>(championships);
        }

        public List<RefereeDto> GetAllReferees()
        {
            List<Referee> referees = _refereeRepository.GetAll();
            return ConversionUtil.GetAllDtosFromModels<Referee, RefereeDto>(referees);
        }

        public List<UntrackedTeamDto> GetAllTeams()
        {
            List<Membership> memberships = _membershipRepository.GetAll();
            List<Team> teams = MembershipUtil.GetAllTeamsFromMultipleMemberships(memberships);
            return ConversionUtil.GetAllDtosFromModels<Team, UntrackedTeamDto>(teams);
        }

        public List<GenericPlayerDto> GetAllPlayers()
        {
            List<Player> referees = _playerRepository.GetAll();
            return ConversionUtil.GetAllDtosFromModels<Player, GenericPlayerDto>(referees);
        }

        public async Task<GameDto> CreateGame(GameDto gameData)
        {
            Game createdGame = ConversionUtil.GetModelFromDto(gameData) as Game;
            await InitCreatedGame(createdGame);
            await _creationCheckerService.EnsureTrackedTeamPlayersInsertion(createdGame.TrackedTeam);
            Game gameToCreate = _creationCheckerService.GetGameToInsertFromData(createdGame);
            await _gameRepository.Insert(gameToCreate);
            createdGame.Id = gameToCreate.Id;
            return ConversionUtil.GetDtoFromModel(createdGame) as GameDto;
        }

        private async Task InitCreatedGame(Game createdGame)
        {
            createdGame.ChampionshipId = await _creationCheckerService.GetEntityCreationDataIdToInsert(createdGame.Championship);
            createdGame.RefereeId = await _creationCheckerService.GetEntityCreationDataIdToInsert(createdGame.Referee);
            createdGame.TrackedTeamId = await _creationCheckerService.GetEntityCreationDataIdToInsert(createdGame.TrackedTeam);
            createdGame.OpponentTeamId = await _creationCheckerService.GetEntityCreationDataIdToInsert(createdGame.OpponentTeam);
        }
    }
}
