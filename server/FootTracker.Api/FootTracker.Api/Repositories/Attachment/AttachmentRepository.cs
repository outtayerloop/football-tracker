using FootTracker.Api.Database;
using FootTracker.Api.Domain.Models;

namespace FootTracker.Api.Repositories
{
    public class AttachmentRepository : FootTrackerRepository<GameAttachment>, IAttachmentRepository
    {
        public AttachmentRepository(FootTrackerDbContext dbContext) : base(dbContext) { }
    }
}
