package com.dongtronic.rolebot

import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.jagrosh.jdautilities.examples.command.AboutCommand
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Activity
import javax.security.auth.login.LoginException

object Main {

    @Throws(LoginException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val token = System.getenv("DISCORD_TOKEN")

        // define an eventwaiter, dont forget to add this to the JDABuilder!
        val waiter = EventWaiter()

        // define a command client
        val client = CommandClientBuilder()

        // The default is "Type !!help" (or whatver prefix you set)
        client.useDefaultGame()
        client.useHelpBuilder(true)

        // sets emojis used throughout the bot on successes, warnings, and failures
        client.setEmojis("\uD83D\uDC4C", "\uD83D\uDE2E", "\uD83D\uDE22")

        client.setPrefix("rolebot")

        client.setOwnerId("189436077793083392")

        // adds commands
        client.addCommands(
                // command to show information about the bot
                AboutCommand(java.awt.Color(0, 0, 255), "A role assignment bot",
                        arrayOf("mass-assign roles based on required roles"),
                        Permission.MANAGE_ROLES),


                AssignCommand())

        // start getting a bot account set up
        JDABuilder(token)
                // set the game for when the bot is loading
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.of(Activity.ActivityType.DEFAULT, "Loading..."))

                // add the listeners
                .addEventListeners(
                        waiter,
                        client.build()
                )
                // start it up!
                .build()


    }

}
