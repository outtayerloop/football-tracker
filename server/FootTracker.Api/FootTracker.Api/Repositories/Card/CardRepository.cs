using FootTracker.Api.Database;
using FootTracker.Api.Domain.Models;

namespace FootTracker.Api.Repositories
{
    public class CardRepository : FootTrackerRepository<Card>, ICardRepository
    {
        public CardRepository(FootTrackerDbContext dbContext) : base(dbContext) { }
    }
}
