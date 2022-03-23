using FootTracker.Api.Domain.Dto;
using FootTracker.Api.Domain.Models;
using FootTracker.Api.Utils;

namespace FootTracker.Api.Converters
{
    public static class TeamConverter
    {

        public static UntrackedTeamDto ConvertToUntrackedDto(Team teamToConvert)
        {
            if (teamToConvert == null)
                return null;

            var convertedTeam = new UntrackedTeamDto();
            convertedTeam.SetCommonMembersFromModel(teamToConvert);
            convertedTeam.Players = ConversionUtil.GetAllGenericPlayersFromModels(teamToConvert.Players);
            return convertedTeam;
        }

        public static TrackedTeamDto ConvertToTrackedDto(Team teamToConvert)
        {
            if (teamToConvert == null)
                return null;

            var convertedTeam = new TrackedTeamDto();
            convertedTeam.SetCommonMembersFromModel(teamToConvert);
            convertedTeam.Players = ConversionUtil.GetAllTrackedPlayersFromModels(teamToConvert.Players);
            return convertedTeam;
        }

        public static Team ConvertToTrackedModel(TrackedTeamDto teamToConvert)
        {
            if (teamToConvert == null)
                return null;

            var convertedTeam = new Team();
            convertedTeam.SetCommonMembersFromDto(teamToConvert);
            convertedTeam.Players = ConversionUtil.GetAllTrackedPlayersFromDtos(teamToConvert.Players);
            return convertedTeam;
        }

        public static Team ConvertToUntrackedModel(UntrackedTeamDto teamToConvert)
        {
            if (teamToConvert == null)
                return null;

            var convertedTeam = new Team();
            convertedTeam.SetCommonMembersFromDto(teamToConvert);
            convertedTeam.Players = ConversionUtil.GetAllGenericPlayersFromDtos(teamToConvert.Players);
            return convertedTeam;
        }
    }
}
