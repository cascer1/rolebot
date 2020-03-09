package com.dongtronic.rolebot

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Role

class AssignCommand() : Command() {

    init {
        this.name = "assign"
        this.help = "Mass-assign roles"
        this.guildOnly = true
        this.arguments = "<required role> <new role>"
        this.userPermissions = arrayOf(Permission.MANAGE_ROLES)
    }

    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.replyWarning("You didn't give me any arguments!")
        } else {
            // split the arguments on all whitespaces
            val items = event.args.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (items.size < 2) {
                event.replyWarning("Required arguments: `current role` and `new role`. For example: `rolebot assign @everyone @awesome`")
                return
            }

            doTheThing(event)
        }
    }

    private fun doTheThing(event: CommandEvent) {
        val roles = event.message.mentionedRoles
        val everyone = event.message.mentionsEveryone()
        val count: Int
        val givenRole: Role

        if (everyone) {
            if (roles.size != 1) {
                event.replyError("Only one role may be assigned at a time")
                return
            }

            givenRole = roles[0]
            count = assignRoles(null, givenRole, event.guild)
        } else {

            if (roles.size != 2) {
                event.replyError("Must provide two roles")
                return
            }

            givenRole = roles[1]
            count = assignRoles(roles[0], givenRole, event.guild)
        }

        event.replySuccess("Assigned role `${givenRole.name}` to $count members")

    }

    private fun assignRoles(required: Role?, assigned: Role, guild: Guild): Int {
        val members = if (required == null) {
            guild.members
        } else {
            guild.getMembersWithRoles(required)
        }

        members.stream().forEach { member -> guild.addRoleToMember(member, assigned).reason("Assigned by Rolebot").queue() }

        return members.size
    }
}
