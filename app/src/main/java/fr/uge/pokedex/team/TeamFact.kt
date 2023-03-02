package fr.uge.pokedex.team

enum class TeamFact(private val category: FactCategory) {
    WEAKNESS_TO_TYPES(FactCategory.WEAKNESS),
    RESISTANCE_TO_TYPES(FactCategory.STRENGTH),
    LOW_BASE_STATS(FactCategory.WEAKNESS),
    HIGH_BASE_STATS(FactCategory.STRENGTH),
    EMPTY_SLOTS(FactCategory.NEITHER),
    BALANCED_BASE_STATS(FactCategory.STRENGTH),
    IMBALANCED_BASE_STATS(FactCategory.WEAKNESS),
    GOOD_TYPE_COVERAGE(FactCategory.STRENGTH);

    private var message: String = ""

    fun setMessage(message: String): TeamFact {
        this.message = message
        return this
    }

    override fun toString(): String {
        return super.toString() + "(${this.category}${ if (message.isNotEmpty()) ", $message" else ""})"
    }
}