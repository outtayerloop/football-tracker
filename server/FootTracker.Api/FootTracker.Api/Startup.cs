using FootTracker.Api.Database;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.EntityFrameworkCore;
using FootTracker.Api.Repositories;
using FootTracker.Api.Services;
using FootTracker.Api.Services.CreationChecker;
using FootTracker.Api.Services.GameReview;
using FootTracker.Api.Services.GameUpdate;

namespace FootTracker.Api
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {

            services.AddControllers();

            //Database
            string connectionString = Configuration.GetConnectionString("FootTrackerDbConnection");
            services.AddDbContext<FootTrackerDbContext>(options => options.UseMySQL(connectionString));

            //Repositories : ajout en scoped car AddDbContext ajoute en scoped (singleton est moins visible que scoped donc exception).
            // Ajout scoped = ajout 1 fois par requete.
            services.AddScoped(typeof(IFootTrackerRepository<>), typeof(FootTrackerRepository<>));
            services.AddScoped<IChampionshipRepository, ChampionshipRepository>();
            services.AddScoped<IRefereeRepository, RefereeRepository>();
            services.AddScoped<ITeamRepository, TeamRepository>();
            services.AddScoped<IPlayerRepository, PlayerRepository>();
            services.AddScoped<IMembershipRepository, MembershipRepository>();
            services.AddScoped<IGameRepository, GameRepository>();
            services.AddScoped<IAttachmentRepository, AttachmentRepository>();
            services.AddScoped<ICardRepository, CardRepository>();
            services.AddScoped<IGoalRepository, GoalRepository>();

            //Services : ajout en scoped pour la meme raison que precedemment.
            services.AddScoped<IGameFormService, GameFormService>();
            services.AddScoped<IGameUpdateService, GameUpdateService>();
            services.AddScoped<ICreationCheckerService, CreationCheckerService>();
            services.AddScoped<IGameReviewService, GameReviewService>();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseRouting();

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}
