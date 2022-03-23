using FootTracker.Api.Domain.Dto.Common;

namespace FootTracker.Api.Domain.Dto
{
    public class CardDto : TimelineDto
    {
        public string Type { get; set; }

        public string Moment { get; set; }

        public int PlayerId { get; set; }
    }
}
