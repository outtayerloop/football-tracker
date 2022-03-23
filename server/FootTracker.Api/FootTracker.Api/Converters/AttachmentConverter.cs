using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Domain.Models;

namespace FootTracker.Api.Converters
{
    public static class AttachmentConverter
    {
        public static GameAttachmentDto ConvertToDto(GameAttachment attachmentToConvert)
        {
            if (attachmentToConvert == null)
                return null;

            return new GameAttachmentDto()
            {
                Id = attachmentToConvert.Id,
                Path = attachmentToConvert.Path,
                GameId = attachmentToConvert.GameId
            };
        }

        public static GameAttachment ConvertToModel(GameAttachmentDto gameAttachmentDto)
        {
            if (gameAttachmentDto == null)
                return null;

            return new GameAttachment()
            {
                Path = gameAttachmentDto.Path,
                GameId = gameAttachmentDto.GameId
            };
        }
    }
}
