using FootTracker.Api.Domain.Dto.Common;

namespace FootTracker.Api.Domain.Dto
{
    public class GameAttachmentDto : TimelineDto
    {
        public string Path { get; set; }
        public int Id { get; set; }

    }
}
