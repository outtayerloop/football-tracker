using System.ComponentModel.DataAnnotations.Schema;

namespace FootTracker.Api.Domain.Models
{
    public class Membership : BaseModel
    {

        [NotMapped]
        public override int Id { get; set; }

        public int PlayerId { get; set; }

        public int TeamId { get; set; }

        public Player Player { get; set; }

        public Team Team { get; set; }
    }
}
