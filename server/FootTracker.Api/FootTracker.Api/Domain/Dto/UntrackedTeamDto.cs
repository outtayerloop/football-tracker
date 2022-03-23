using System.Collections.Generic;

namespace FootTracker.Api.Domain.Dto
{
    public class UntrackedTeamDto : BaseTeamDto
    {
        public List<GenericPlayerDto> Players { get; set; }
    }
}
