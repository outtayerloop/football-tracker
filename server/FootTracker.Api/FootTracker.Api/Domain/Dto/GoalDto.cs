using FootTracker.Api.Domain.Dto.Common;

namespace FootTracker.Api.Domain.Dto
{
    public class GoalDto : TimelineDto
    {
        public int PlayerId { get; set; }

        public string Moment { get; set; }
    }
}
