namespace FootTracker.Api.Domain
{
    /// <summary>
    /// Poste occupe par un joueur dans une equipe.
    /// </summary>
    public enum PlayerPosition
    {
        /// <summary>
        /// Avant-centre.
        /// </summary>
        Striker = 0,

        /// <summary>
        /// Ailier gauche.
        /// </summary>
        LeftMidfielder = 1,

        /// <summary>
        /// Ailier droit.
        /// </summary>
        RightMidfielder = 2,

        /// <summary>
        /// Milieu gauche.
        /// </summary>
        AttackingMidfielder = 3,

        /// <summary>
        /// Milieu droit.
        /// </summary>
        CentralMidfielder = 4,

        /// <summary>
        /// Milieu central.
        /// </summary>
        DefendingMidfielder = 5,

        /// <summary>
        /// Arriere gauche.
        /// </summary>
        LeftFullBack = 6,

        /// <summary>
        /// Arriere droit.
        /// </summary>
        RightFullBack = 7,

        /// <summary>
        /// Defenseur central gauche.
        /// </summary>
        LeftCenterBack = 8,

        /// <summary>
        /// Defenseur central droit.
        /// </summary>
        RightCenterBack = 9,

        /// <summary>
        /// Gardien de but.
        /// </summary>
        Goalkeeper = 10

    }
}
