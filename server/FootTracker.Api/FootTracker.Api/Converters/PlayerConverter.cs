using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Domain.Models;
using System.Linq;

namespace FootTracker.Api.Converters
{
    public static class PlayerConverter
    {
        public static TrackedPlayerDto ConvertToTrackedDto(Player playerToConvert)
        {
            if (playerToConvert == null)
                return null;

            var convertedPlayer = new TrackedPlayerDto();
            convertedPlayer.SetCommonMembersFromModel(playerToConvert);
            convertedPlayer.SetCardsFromModelList(playerToConvert.Cards?.ToList());
            convertedPlayer.SetGoalsFromModelList(playerToConvert.Goals?.ToList());
            return convertedPlayer;
        }

        public static GenericPlayerDto ConvertToUntrackedDto(Player playerToConvert)
        {
            if (playerToConvert == null)
                return null;

            var convertedPlayer = new GenericPlayerDto();
            convertedPlayer.SetCommonMembersFromModel(playerToConvert);
            return convertedPlayer;
        }

        public static Player ConvertToTrackedModel(TrackedPlayerDto playerToConvert)
        {
            if (playerToConvert == null)
                return null;

            var convertedPlayer = new Player();
            convertedPlayer.SetCommonMembersFromDto(playerToConvert);
            convertedPlayer.SetCardsFromDtoList(playerToConvert.Cards?.ToList());
            convertedPlayer.SetGoalsFromDtoList(playerToConvert.Goals?.ToList());
            return convertedPlayer;
        }

        public static Player ConvertToUntrackedModel(GenericPlayerDto playerToConvert)
        {
            if (playerToConvert == null)
                return null;

            var convertedPlayer = new Player();
            convertedPlayer.SetCommonMembersFromDto(playerToConvert);
            return convertedPlayer;
        }
    }
}
