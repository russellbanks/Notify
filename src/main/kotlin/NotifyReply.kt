import dev.kord.core.entity.Member

object NotifyReply {

    /**
     * Builds the message that tags everyone and states what members are in a voice channel
     *
     * @param member [Member] - The member that ran the command
     */
    suspend fun getValidReply(member: Member) = "@everyone, ${member.mention} is in **${getChannelName(member)}** ${
        getFormattedListOfMembers(getListOfVCMembers(member))
    }"

    /**
     * Builds the message that tells the user that they need to be in a voice channel to use the command
     *
     * @param member [Member] - The member that ran the command
     */
    fun getInvalidReply(member: Member) = "${member.mention}, you must be in a voice channel to use this command."

    /**
     * Gets a mutable list of a valid members inside the voice channel that the member is in
     *
     * @param member [Member] - The member that ran the command
     */
    private suspend fun getListOfVCMembers(member: Member) = mutableListOf<String>().also { otherMemberMentions ->
        member.getVoiceState().getChannelOrNull()?.voiceStates?.collect { voiceState ->
            voiceState.getMemberOrNull()?.run {
                if (this != member && !isBot) otherMemberMentions.add(nicknameMention)
            }
        }
    }

    /**
     * Formats a mutable list of voice members inside a voice channel into a readable string
     *
     * @param listOfVoiceMembers [MutableList] - A list of valid members inside a voice channel
     */
    private fun getFormattedListOfMembers(listOfVoiceMembers: MutableList<String>) = listOfVoiceMembers.run {
        when (size) {
            0 -> ""
            1 -> "with ${first()}"
            else -> {
                val finalMember = last()
                removeLast()
                "with ${joinToString()} and $finalMember"
            }
        }
    }

    /**
     * Gets the name of the voice channel that the member is currently in
     *
     * @param member [Member] - The member that ran the command
     */
    private suspend fun getChannelName(member: Member) = bot.client.getChannel(member.getVoiceState().channelId!!)?.data?.name?.value
}