using System.Collections.Generic;

namespace FootTracker.Api.Domain.Models
{
    public class Referee : BaseModel
    {
        public string FullName { get; set; }

        public ICollection<Game> Games { get; set; }
    }
}
