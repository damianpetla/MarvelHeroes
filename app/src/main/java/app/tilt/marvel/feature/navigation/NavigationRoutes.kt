package app.tilt.marvel.feature.navigation

object NavigationRoutes {

    val HeroCollection = "herocollection"
    val HeroDetailsHeroIdParam = "heroId"
    val HeroDetails = "herodetails/{${HeroDetailsHeroIdParam}}"

    fun heroDetailsParam(heroId: Int) = "herodetails/$heroId"
}
