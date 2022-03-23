using FootTracker.Api.Database;
using FootTracker.Api.Domain.Models;

namespace FootTracker.Api.Repositories
{
    public class PlayerRepository : FootTrackerRepository<Player>, IPlayerRepository
    {
        public PlayerRepository(FootTrackerDbContext dbContext) : base(dbContext) { }
    }
}
