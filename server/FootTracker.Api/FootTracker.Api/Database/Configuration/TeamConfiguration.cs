using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;

namespace FootTracker.Api.Database.Configuration
{
    public class TeamConfiguration : BaseConfiguration
    {
        public TeamConfiguration(ModelBuilder modelBuilder) : base(modelBuilder) { }

        public override void Configure()
        {
            _modelBuilder.Entity<Team>()
                .ToTable("TEAMS")
                .HasKey(team => team.Id)
                .HasName("PK_Team_Id");

            _modelBuilder.Entity<Team>()
                .Property(team => team.Id)
                .HasColumnName("team_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Team>()
                .Property(team => team.Name)
                .HasColumnName("team_name")
                .HasColumnType("VARCHAR(100)")
                .IsRequired(true);

            _modelBuilder.Entity<Team>()
                .HasIndex(team => team.Name)
                .IsUnique()
                .HasDatabaseName("UC_Team_Name");

            _modelBuilder.Entity<Team>()
                .HasMany(trackedTeam => trackedTeam.TrackedTeamGames)
                .WithOne(game => game.TrackedTeam)
                .HasForeignKey(game => game.TrackedTeamId)
                .HasConstraintName("FK_Game_Tracked_Team_Id")
                .OnDelete(DeleteBehavior.Cascade);

            _modelBuilder.Entity<Team>()
                .HasMany(opponentTeam => opponentTeam.OpponentTeamGames)
                .WithOne(game => game.OpponentTeam)
                .HasForeignKey(game => game.OpponentTeamId)
                .HasConstraintName("FK_Game_Opponent_Team_Id")
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
