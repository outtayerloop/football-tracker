using FootTracker.Api.Domain.Models;
using Microsoft.EntityFrameworkCore;

namespace FootTracker.Api.Database.Configuration
{
    public class GoalConfiguration : BaseConfiguration
    {
        public GoalConfiguration(ModelBuilder modelBuilder) : base(modelBuilder) { }

        public override void Configure()
        {
            _modelBuilder.Entity<Goal>()
                .ToTable("GOALS")
                .HasKey(goal => new { goal.PlayerId, goal.GameId, goal.Moment })
                .HasName("PK_Goal_Id");

            _modelBuilder.Entity<Goal>()
                .HasOne(goal => goal.Game)
                .WithMany(game => game.Goals)
                .HasForeignKey(goal => goal.GameId)
                .HasConstraintName("FK_Goal_Game_Id");

            _modelBuilder.Entity<Goal>()
                .Property(goal => goal.GameId)
                .HasColumnName("goal_game_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Goal>()
                .Property(goal => goal.PlayerId)
                .HasColumnName("goal_player_id")
                .HasColumnType("INT")
                .IsRequired(true);

            _modelBuilder.Entity<Goal>()
                .HasOne(goal => goal.Player)
                .WithMany(player => player.Goals)
                .HasForeignKey(goal => goal.PlayerId)
                .HasConstraintName("FK_Goal_Player_Id");

            _modelBuilder.Entity<Goal>()
                .Property(goal => goal.Moment)
                .HasColumnName("moment")
                .HasColumnType("TIME")
                .IsRequired(true);
        }
    }
}
