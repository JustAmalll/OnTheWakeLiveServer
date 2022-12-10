package com.onthewake.routes

import com.onthewake.data.model.trick_list.TrickItem
import com.onthewake.data.model.trick_list.TrickList
import com.onthewake.data.trick_list.TrickListSource
import com.onthewake.requests.TrickListRequest
import com.onthewake.requests.toTrickItem
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val trickList = TrickList(
    spins = listOf(
        TrickItem(
            name = "180",
            description = "a 180 degree spin where the rider switches foot forward can be FS or BS"
        ),
        TrickItem(
            name = "360",
            description = "where the rider does a 360 degree spin. Can be FS or BS and handle passed, wrapped or baller"
        ),
        TrickItem(
            name = "540",
            description = "where the rider does a 540 degree spin. Can be FS or BS and handle passed, wrapped or baller"
        ),
        TrickItem(
            name = "720",
            description = "where the rider does a 720 degree spin. Can be FS or BS and handle passed, wrapped or baller"
        ),
        TrickItem(
            name = "900",
            description = "where the rider does a 900 degree spin. Can be FS or BS and handle passed, wrapped or baller"
        ),
        TrickItem(
            name = "1080",
            description = "where the rider does a 1080 degree spin. Can be FS or BS and handle passed or wrapped"
        )
    ),
    raileyTricks = listOf(
        TrickItem(
            name = "railey",
            description = "releasing from a HS edge the board is thrown behind the rider above the head in a superman style"
        ),
        TrickItem(
            name = "911",
            description = "a BS 90 degree tweeked railey"
        ),
        TrickItem(
            name = "Butter fuko",
            description = "a 911 with a FS 180"
        ),
        TrickItem(
            name = "krypt",
            description = "a railey where the rider does a FS 180 to land opposite foot forward"
        ),
        TrickItem(
            name = "hoochie",
            description = "a railey where the rider grabs the board with his front hand on the heel side edge"
        ),
        TrickItem(
            name = "OHH",
            description = "a railey where the rider grabs the board with his back hand on the heel side edge"
        ),
        TrickItem(
            name = "Blind Judge",
            description = "a railey where the rider does a BS 180"
        ),
        TrickItem(
            name = "313",
            description = "a railey where the rider does a FS 360 handle pass"
        ),
        TrickItem(
            name = "BS 313",
            description = "a railey where the rider does a BS 360 handle pass"
        ),
        TrickItem(
            name = "Nickelodeon",
            description = "a railey where the rider does a handle pass 540"
        ),
        TrickItem(
            name = "S-bend",
            description = "a railey where the rider does a BS 360 with handle above their head"
        ),
        TrickItem(
            name = "Volcan",
            description = "a S-bend with a rewind frontside 180"
        ),
        TrickItem(
            name = "S-bend to blind",
            description = "an s bend with an extra BS 180"
        ),
        TrickItem(
            name = "double S bend",
            description = "a railey with a BS 720 with the handle above their head"
        ),
        TrickItem(
            name = "double S bend to blind",
            description = "a double S-bend with an extra BS 180"
        ),
        TrickItem(
            name = "S-mobe",
            description = "an S-bend with a FS handle pass 360"
        ),
        TrickItem(
            name = "Heart attack",
            description = "an S-bend with a BS handle pass 360"
        ),
        TrickItem(
            name = "rubber chicken",
            description = "a railey with an ole BS 360"
        ),
        TrickItem(
            name = "hinterberger",
            description = "a railey with a FS 360 with the handle above their head"
        ),
        TrickItem(
            name = "hinterberger 5",
            description = "a hinterberger with an extra FS 180"
        ),
        TrickItem(
            name = "hinterberger to blind",
            description = "a hinterberger with a BS 180"
        ),
        TrickItem(
            name = "118",
            description = "a double hinterberger (720 rotation)"
        ),
        TrickItem(
            name = "118 900",
            description = "a 118 with an extra FS 180"
        ),
        TrickItem(
            name = "TS railey",
            description = "TS edge railey"
        ),
        TrickItem(
            name = "TS Krypt",
            description = "a TS railey where the rider does a FS 180 to land the opporstie foot forward"
        ),
        TrickItem(
            name = "90210",
            description = "a TS railey where the rider does a FS 360"
        ),
        TrickItem(
            name = "TS blind judge",
            description = "a TS railey with a BS 180"
        )
    ),
    backRollTricks = listOf(
        TrickItem(
            name = "Back roll",
            description = "a HS release where the board rotates nose over tail (reverse cartwheel)"
        ),
        TrickItem(
            name = "Roll to revert",
            description = "a back roll with a FS 180"
        ),
        TrickItem(
            name = "Roll to blind",
            description = "a back roll with a BS 180"
        ),
        TrickItem(
            name = "Mobe",
            description = "a back roll with a FS 360"
        ),
        TrickItem(
            name = "Mobe 5",
            description = "a back roll with a FS 540 handle pass"
        ),
        TrickItem(
            name = "KGB",
            description = "a back roll with a BS 360 handle pass"
        ),
        TrickItem(
            name = "Big Mac",
            description = "Ole KGB"
        ),
        TrickItem(
            name = "TS back roll",
            description = "a TS backflip (tantrum)"
        ),
        TrickItem(
            name = "TS back roll to revert",
            description = "a TS back roll with a FS 180"
        ),
        TrickItem(
            name = "TS back roll to blind",
            description = "a TS back roll with a BS 180"
        ),
        TrickItem(
            name = "pete rose",
            description = "a TS backroll with a FS 360"
        )
    ),
    frontFlipTricks = listOf(
        TrickItem(
            name = "Front Flip",
            description = "HS approach where the rider flips tail over nose (cartwheel)"
        ),
        TrickItem(
            name = "Front to fakie",
            description = "a front flip with a FS 180"
        ),
        TrickItem(
            name = "Front to Blind",
            description = "a front flip with a BS 180"
        ),
        TrickItem(
            name = "Front blind mobe",
            description = "a front flip with a BS handle pass 360"
        ),
        TrickItem(
            name = "Slim chance",
            description = "a front flip with a FS handle pass 360"
        )
    ),
    frontRollTricks = listOf(
        TrickItem(
            name = "Front roll",
            description = "a TS front roll with the board rotating heel over toe"
        ),
        TrickItem(
            name = "scarcrow",
            description = "a front roll with a FS 180"
        ),
        TrickItem(
            name = "elephant",
            description = "a scarcrow with a rewind 180 landng origional foot forward"
        ),
        TrickItem(
            name = "crow mobe",
            description = "a front roll with a FS 360"
        ),
        TrickItem(
            name = "crow mobe 5",
            description = "a front roll with a handle pass FS 540"
        ),
        TrickItem(
            name = "tootsie",
            description = "a front roll with a BS handle pass180"
        ),
        TrickItem(
            name = "dum dum",
            description = "a front roll with a BS handle pass 360"
        ),
        TrickItem(
            name = "Mexican roll",
            description = "HS front roll heel edge over toe edge"
        )
    ),
    tantrumTricks = listOf(
        TrickItem(
            name = "tantrum",
            description = "a HS back flip"
        ),
        TrickItem(
            name = "tantrum to fakie",
            description = "a tantrum with a FS 180"
        ),
        TrickItem(
            name = "tantrum to blnd ",
            description = "a tantrum with a BS 180"
        ),
        TrickItem(
            name = "moby dick",
            description = "a tantrum with a BS handle pass 360"
        ),
        TrickItem(
            name = "wirly bird",
            description = "a tantrum with a BS ole 360"
        ),
        TrickItem(
            name = "whirly 5",
            description = "a whirly bird with an extra BS 180"
        ),
        TrickItem(
            name = "whirly dick",
            description = "a whirly bird with a handle pass BS 360"
        ),
        TrickItem(
            name = "double whirly",
            description = "a double whirly bird ( 720 ole)"
        )
    ),
    whipTricks = listOf(
        TrickItem(
            name = "bell air",
            description = "a heelside back flip releasing from the water off your toesde rail"
        ),
        TrickItem(
            name = "bell air to fakie",
            description = "bell air FS 180"
        ),
        TrickItem(
            name = "bell air to blind",
            description = "bell air BS 180"
        ),
        TrickItem(
            name = "moby dick",
            description = "a bell air with a BS handle pass 360"
        ),
        TrickItem(
            name = "tweetie",
            description = "a whirly bird off a bell air edge"
        ),
        TrickItem(
            name = "tweetie 5",
            description = "a tweetie bird with an extra BS 180"
        ),
        TrickItem(
            name = "tweetie dick",
            description = "a tweetie bird with a BS handle pass 360"
        ),
        TrickItem(
            name = "Ben air",
            description = "a front roll releasing from the water off your heelside edge"
        ),
        TrickItem(
            name = "egg roll",
            description = "a scare crow releasing from the water off your heelside edge"
        ),
        TrickItem(
            name = "egg mobe",
            description = "an egg roll with an extra BS 180"
        ),
        TrickItem(
            name = "tootsie",
            description = "a ben ar with an extra BS 180"
        )
    ),
    grabs = listOf(
        TrickItem(
            name = "Indy",
            description = "Back hand between the feet toeside edge."
        ),
        TrickItem(
            name = "Tindy",
            description = "Back hand toeside edge between back foot and the tail of the board"
        ),
        TrickItem(
            name = "Tail",
            description = "Back hand, grab tail edge of the board."
        ),
        TrickItem(
            name = "Tailfish",
            description = "back hand heelside edge between the back binding and the tail"
        ),
        TrickItem(
            name = "Stalefish",
            description = "Back hand heelside edge between the bindings."
        ),
        TrickItem(
            name = "Melon",
            description = "Front hand heelside edge between the bindings"
        ),
        TrickItem(
            name = "Mute",
            description = "Front hand heelside edge between front binding and the nose, frontside tweak."
        ),
        TrickItem(
            name = "Method",
            description = "Front hand heelside edge between front binding and the nose, backside tweak"
        ),
        TrickItem(
            name = "Nose",
            description = "Front hand, grab nose edge of the board."
        ),
        TrickItem(
            name = "Slob",
            description = "Front hand toeside edge between front foot and the nose."
        ),
        TrickItem(
            name = "Crail",
            description = "Back hand, between the front binding and the nose of the board, toeseide edge."
        ),
        TrickItem(
            name = "Nuclear",
            description = "Back hand, heelside edge between the front binding and the nose"
        ),
        TrickItem(
            name = "Seat Belt",
            description = "Front hand, heelside edge, between the back binding and the tail of the board."
        ),
        TrickItem(
            name = "Roast beef",
            description = "Back hand, heelside edge between the bindings, elbow facing heel edge."
        ),
        TrickItem(
            name = "Chicken salad",
            description = "Back hand, heelside edge between the bindings, elbow facing toe edge."
        )
    ),
    rails = listOf(
        TrickItem(
            name = "5050",
            description = "HS or TS approach riding along the rail with the nose of the board facing up the line"
        ),
        TrickItem(
            name = "BS board slide",
            description = "HS approach, nose over the rail (frontside 90degree rotation), facing up the line"
        ),
        TrickItem(
            name = "front lip",
            description = "TS approach, tail over the rail (frontside 90degree rotation), facing up the line"
        ),
        TrickItem(
            name = "back lip",
            description = "HS approach, tail over the rail (backside 90degree rotation), facing away from the line"
        ),
        TrickItem(
            name = "front board",
            description = "TS approach, nose over the rail (backside 90degree rotation), facing away from the line"
        )
    )
)

fun Route.getTrickList() {
    get("/trick_list") {
        call.respond(
            status = HttpStatusCode.OK,
            message = trickList
        )
    }
}

fun Route.getUsersTrickList(
    trickListSource: TrickListSource
) {
    authenticate {
        get("/users/trick_list") {
            val userId = call.parameters["userId"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val trickList = trickListSource.getUsersTrickList(userId)
            if (trickList == null) {
                call.respond(HttpStatusCode.Conflict)
                return@get
            }

            call.respond(
                status = HttpStatusCode.OK,
                message = trickList
            )
        }
    }
}

fun Route.addTrickList(
    trickListSource: TrickListSource
) {
    authenticate {
        post("add/trick_list") {
            val request = call.receive<TrickListRequest>()

            val trickList = trickListSource.getUsersTrickList(call.userId)

            val trickListRequest = TrickList(
                userId = call.userId,
                spins = request.spins.map { it.toTrickItem() },
                raileyTricks = request.raileyTricks.map { it.toTrickItem() },
                backRollTricks = request.backRollTricks.map { it.toTrickItem() },
                frontFlipTricks = request.frontFlipTricks.map { it.toTrickItem() },
                frontRollTricks = request.frontRollTricks.map { it.toTrickItem() },
                tantrumTricks = request.tantrumTricks.map { it.toTrickItem() },
                whipTricks = request.whipTricks.map { it.toTrickItem() },
                grabs = request.grabs.map { it.toTrickItem() },
                rails = request.rails.map { it.toTrickItem() }
            )

            val wasAcknowledged = if (trickList != null) {
                trickListSource.updateTrickList(call.userId, trickList.id, trickListRequest)
            } else {
                trickListSource.addTrickList(trickListRequest)
            }

            if (!wasAcknowledged) {
                call.respond(HttpStatusCode.Conflict)
                return@post
            }

            call.respond(HttpStatusCode.OK)
        }
    }
}