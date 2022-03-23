using FootTracker.Api.Database;
using FootTracker.Api.Domain.Models;

namespace FootTracker.Api.Repositories
{
    public class RefereeRepository : FootTrackerRepository<Referee>, IRefereeRepository
    {
        public RefereeRepository(FootTrackerDbContext dbContext) : base(dbContext) { }
    }
}
