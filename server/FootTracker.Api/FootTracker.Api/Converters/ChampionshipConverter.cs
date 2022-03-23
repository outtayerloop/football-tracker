using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Domain.Models;

namespace FootTracker.Api.Converters
{
    public static class ChampionshipConverter
    {
        public static ChampionshipDto ConvertToDto(Championship championshipToConvert)
        {
            if (championshipToConvert == null)
                return null;

            return new ChampionshipDto() { 
                Id = championshipToConvert.Id, 
                Name = championshipToConvert.Name 
            };
        }

        public static Championship ConvertToModel(ChampionshipDto championshipToConvert)
        {
            if (championshipToConvert == null)
                return null;

            return new Championship()
            {
                Id = championshipToConvert.Id,
                Name = championshipToConvert.Name
            };
        }
    }
}
