using FootTracker.Api.Domain.Models;
using FootTracker.Api.Repositories;
using FootTracker.Api.Utils;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FootTracker.Api.Services.CreationChecker
{
    public class CreationCheckerService : ICreationCheckerService
    {
        private readonly IChampionshipRepository _championshipRepository;
        private readonly IRefereeRepository _refereeRepository;
        private readonly ITeamRepository _teamRepository;
        private readonly IMembershipRepository _membershipRepository;
        private readonly IPlayerRepository _playerRepository;

        public CreationCheckerService(IChampionshipRepository championshipRepository, IRefereeRepository refereeRepository,
            ITeamRepository teamRepository, IMembershipRepository membershipRepository,
            IPlayerRepository playerRepository)
        {
            _championshipRepository = championshipRepository;
            _refereeRepository = refereeRepository;
            _teamRepository = teamRepository;
            _membershipRepository = membershipRepository;
            _playerRepository = playerRepository;
        }

        public async Task<int> GetEntityCreationDataIdToInsert<TModel>(TModel entityCreationData) where TModel : BaseModel
        {
            if (entityCreationData.Id > 0)
                return await Task.Run(() => entityCreationData.Id);
            IFootTrackerRepository<TModel> repository = GetModelRepository<TModel, IFootTrackerRepository<TModel>>(entityCreationData);
            TModel createdModel = await repository.Insert(entityCreationData);
            return createdModel.Id ;
        }

        public async Task EnsureTrackedTeamPlayersInsertion(Team trackedTeam)
        {
            if (!trackedTeam.HasPlayers())
                throw new ArgumentException("Tracked team must have players.");
            List<Player> existingTeamPlayers = _membershipRepository.GetTeamPlayersById(trackedTeam.Id);
            if (existingTeamPlayers.Count == 0)
                await InsertTrackedTeamPlayers(trackedTeam);
        }

        // Important pour la creation, mettre tous les membres de type "classe" à null sinon violation de clé primaire.
        public Game GetGameToInsertFromData(Game gameData)
        {
            return new Game
            {
                DateTime = gameData.DateTime,
                Address = gameData.Address,
                OverTime = gameData.OverTime,
                TrackedTeamId = gameData.TrackedTeamId,
                OpponentTeamId = gameData.OpponentTeamId,
                RefereeId = gameData.RefereeId,
                ChampionshipId = gameData.ChampionshipId
            };
        }

        private TRepository GetModelRepository<TModel, TRepository>(TModel model)
            where TModel : BaseModel
            where TRepository : IFootTrackerRepository<TModel>
        {
            return model switch
            {
                Championship _ => (TRepository)_championshipRepository,
                Referee _ => (TRepository)_refereeRepository,
                Team _ => (TRepository)_teamRepository,
                _ => throw new NotImplementedException("No repository found for given model")
            };
        }

        private async Task InsertTrackedTeamPlayers(Team trackedTeam)
        {
            for (int i = 0; i < trackedTeam.Players.Count; ++i)
            {
                trackedTeam.Players[i].Id = await GetNewTeamPlayerId(trackedTeam.Players[i]);
                var newMembership = MembershipUtil.GetMembershipFromData(trackedTeam, trackedTeam.Players[i]);
                await _membershipRepository.Insert(newMembership);
            }
        }

        private async Task<int> GetNewTeamPlayerId(Player newTeamPlayer)
        {
            if (newTeamPlayer.Id > 0)
                return await Task.Run(() => newTeamPlayer.Id);
            else
            {
                newTeamPlayer = await _playerRepository.Insert(newTeamPlayer);
                return newTeamPlayer.Id;
            }
        }
    }
}
