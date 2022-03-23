using Microsoft.AspNetCore.Mvc;

namespace FootTracker.Api.Controllers
{
    /// <summary>
    /// Controleur par defaut utilise lors du lancement du serveur (voir launchSettings.json dans le dossier Properties).
    /// </summary>
    [Route("[controller]")]
    public class DefaultController : ControllerBase
    {
        [HttpGet]
        public ActionResult Default()
            => Ok(new { message = "Server running and listening on port 4000" });
    }
}
