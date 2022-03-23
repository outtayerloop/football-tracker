using FootTracker.Api.Database;
using FootTracker.Api.Domain.Models;

namespace FootTracker.Api.Repositories
{
    public class ChampionshipRepository : FootTrackerRepository<Championship>, IChampionshipRepository
    {
        public ChampionshipRepository(FootTrackerDbContext dbContext) : base(dbContext) { }
    }
}
