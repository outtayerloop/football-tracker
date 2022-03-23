using System.Collections.Generic;

namespace FootTracker.Api.Domain.Dto
{
    public class TrackedTeamDto : BaseTeamDto
    {
        public List<TrackedPlayerDto> Players { get; set; }
    }
}
