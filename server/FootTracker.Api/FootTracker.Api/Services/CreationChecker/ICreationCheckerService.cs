using FootTracker.Api.Domain.Models;
using System.Threading.Tasks;

namespace FootTracker.Api.Services.CreationChecker
{
    public interface ICreationCheckerService
    {
        Task<int> GetEntityCreationDataIdToInsert<TModel>(TModel entityCreationData) where TModel : BaseModel;

        Task EnsureTrackedTeamPlayersInsertion(Team trackedTeam);

        Game GetGameToInsertFromData(Game gameData);
    }
}
