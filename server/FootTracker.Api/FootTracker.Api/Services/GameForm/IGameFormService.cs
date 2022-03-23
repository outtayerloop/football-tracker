using FootTracker.Api.Domain.Dto;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FootTracker.Api.Services
{
    public interface IGameFormService
    {
        List<ChampionshipDto> GetAllChampionships();

        List<RefereeDto> GetAllReferees();

        List<UntrackedTeamDto> GetAllTeams();

        List<GenericPlayerDto> GetAllPlayers();

        Task<GameDto> CreateGame(GameDto gameData);
    }
}
