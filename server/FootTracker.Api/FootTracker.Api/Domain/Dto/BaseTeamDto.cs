using FootTracker.Api.Domain.Models;

namespace FootTracker.Api.Domain.Dto
{
    public abstract class BaseTeamDto : IdentifiedDto
    {
        public string Name { get; set; }

        public void SetCommonMembersFromModel(Team teamToConvert)
        {
            Id = teamToConvert.Id;
            Name = teamToConvert.Name;
        }
    }
}
