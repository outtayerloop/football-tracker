using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;

namespace FootTracker.Api.Database.Configuration
{
    public class MembershipConfiguration : BaseConfiguration
    {
        public MembershipConfiguration(ModelBuilder modelBuilder) : base(modelBuilder) { }

        public override void Configure()
        {
            _modelBuilder.Entity<Membership>()
                .ToTable("MEMBERSHIPS")
                .HasKey(membership => new { membership.PlayerId, membership.TeamId })
                .HasName("PK_Membership_Id");

            _modelBuilder.Entity<Membership>()
                .Property(membership => membership.PlayerId)
                .HasColumnName("membership_player_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Membership>()
                .Property(membership => membership.TeamId)
                .HasColumnName("membership_team_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Membership>()
                .HasOne(membership => membership.Player)
                .WithMany(player => player.Memberships)
                .HasForeignKey(membership => membership.PlayerId)
                .HasConstraintName("FK_Membership_player_id");

            _modelBuilder.Entity<Membership>()
                .HasOne(membership => membership.Team)
                .WithMany(team => team.Memberships)
                .HasForeignKey(membership => membership.TeamId)
                .HasConstraintName("FK_Membership_team_id");
        }
    }
}
