package app.tilt.marvel.feature.herodetails

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension

fun enteredConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val image = createRefFor("image")
        val description = createRefFor("description")
        val name = createRefFor("name")
        val comics = createRefFor("comics")
        constrain(name) {
            start.linkTo(image.start)
            end.linkTo(image.end)
            top.linkTo(image.top)
            width = Dimension.fillToConstraints
        }
        constrain(image) {
            start.linkTo(parent.start, 64.dp)
            end.linkTo(parent.end, 64.dp)
            top.linkTo(parent.top, 128.dp)
            width = Dimension.fillToConstraints
        }
        constrain(description) {
            start.linkTo(parent.end)
        }
        constrain(comics) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.bottom)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }
    }
}

fun finishedConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val image = createRefFor("image")
        val description = createRefFor("description")
        val name = createRefFor("name")
        val comics = createRefFor("comics")
        val bottomBarrier = createBottomBarrier(image, description)
        constrain(name) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top, 16.dp)
            width = Dimension.fillToConstraints
        }
        constrain(image) {
            start.linkTo(parent.start, 16.dp)
            top.linkTo(name.bottom, 16.dp)
            width = Dimension.percent(0.4f)
        }
        constrain(description) {
            start.linkTo(image.end, 16.dp)
            top.linkTo(name.bottom, 16.dp)
            end.linkTo(parent.end, 16.dp)
            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        }
        constrain(comics) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(bottomBarrier, 16.dp)
            bottom.linkTo(parent.bottom)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }
    }
}
