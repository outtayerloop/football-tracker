using FootTracker.Api.Database;
using FootTracker.Api.Domain.Models;

namespace FootTracker.Api.Repositories
{
    public class TeamRepository : FootTrackerRepository<Team>, ITeamRepository
    {
        public TeamRepository(FootTrackerDbContext dbContext) : base(dbContext) { }
    }
}
