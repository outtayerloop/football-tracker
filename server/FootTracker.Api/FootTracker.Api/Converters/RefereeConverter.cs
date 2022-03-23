using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Domain.Models;

namespace FootTracker.Api.Converters
{
    public static class RefereeConverter
    {

        public static RefereeDto ConvertToDto(Referee refereeToConvert)
        {
            if (refereeToConvert == null)
                return null;

            return new RefereeDto() 
            { 
                Id = refereeToConvert.Id,
                Name = refereeToConvert.FullName
            };
        }

        public static Referee ConvertToModel(RefereeDto refereeToConvert)
        {
            if (refereeToConvert == null)
                return null;

            return new Referee()
            {
                Id = refereeToConvert.Id,
                FullName = refereeToConvert.Name
            };
        }
    }
}
