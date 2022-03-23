using Microsoft.EntityFrameworkCore;

namespace FootTracker.Api.Database.Configuration
{
    public abstract class BaseConfiguration
    {

        protected readonly ModelBuilder _modelBuilder;

        public BaseConfiguration(ModelBuilder modelBuilder)
            => _modelBuilder = modelBuilder;

        public abstract void Configure();
    }
}
