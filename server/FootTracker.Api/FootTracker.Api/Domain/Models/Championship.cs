using System.Collections.Generic;

namespace FootTracker.Api.Domain.Models
{
    public class Championship : BaseModel
    {
        public string Name { get; set; }

        public ICollection<Game> Games { get; set; }
    }
}
