package fr.uge.pokedex.service.team

enum class TeamFact(val category: FactCategory) {
    WEAKNESS_TO_TYPES(FactCategory.WEAKNESS),
    RESISTANCE_TO_TYPES(FactCategory.STRENGTH),
    LOW_BASE_STATS(FactCategory.WEAKNESS),
    HIGH_BASE_STATS(FactCategory.STRENGTH),
    EMPTY_SLOTS(FactCategory.INFO),
    BALANCED_BASE_STATS(FactCategory.STRENGTH),
    IMBALANCED_BASE_STATS(FactCategory.WEAKNESS),
    GOOD_TYPE_COVERAGE(FactCategory.STRENGTH);

    private var message: String = ""

    fun setMessage(message: String): TeamFact {
        this.message = message
        return this
    }

    fun getMessage(): String {
        return message
    }

    override fun toString(): String {
        return super.toString() + "(${message.ifEmpty { "" }})"
    }
}