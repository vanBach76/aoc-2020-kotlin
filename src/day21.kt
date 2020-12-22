import java.io.File

fun main() {
    val input = File("./input/day21.txt").readText()
    part1(input.split("\r\n"))
}


private fun part1(input: List<String>) {
    val foodToAllergensMap = toIngredientsMap(input)
    val allergens = mutableSetOf<String>()
    foodToAllergensMap.values.forEach { value -> allergens.addAll(value) }
    val allergenToIngredientMap = mutableMapOf<String, String>()
    var missingAllergens = allergens.toMutableList()
    while(missingAllergens.size > 0) {
        for(allergen in missingAllergens) {
            val inFoods = foodToAllergensMap.filter { entry -> entry.value.contains(allergen) }.keys.toList()
            var candidates = inFoods.first()
            for(i in 1 until inFoods.size) {
                candidates = candidates.intersect(inFoods[i]).toMutableList()
            }
            candidates.removeAll(allergenToIngredientMap.values)
            if(candidates.size == 1) allergenToIngredientMap[allergen] = candidates[0] else println("Could not find any ingredient for allergen: $allergen")
        }
        missingAllergens.removeAll(allergenToIngredientMap.keys)
    }
    println(allergenToIngredientMap)

    val ingredients = mutableSetOf<String>()
    foodToAllergensMap.keys.forEach { key -> ingredients.addAll(key) }

    var count = 0
    ingredients.removeAll(allergenToIngredientMap.values)
    ingredients.forEach {
        ingredient -> count += foodToAllergensMap.keys.filter { food -> food.contains(ingredient) }.count()}

    println("Number ingredients without allergens: $count")

    val sortedAllergens = allergenToIngredientMap.keys.sorted()
    println(sortedAllergens)

    val dangerList = sortedAllergens.map { allergen -> allergenToIngredientMap[allergen] }.joinToString().replace(" ", "")
    println("Canonical dangerous ingredient list : $dangerList")

}

private fun toIngredientsMap(input: List<String>): MutableMap<MutableList<String>, MutableList<String>> {
    val ingredientsMap = mutableMapOf<MutableList<String>, MutableList<String>>()
    for (line in input) {
        val split = line.split(" (")
        val ingredients = split[0].split(" ").toMutableList()
        val allergens = split[1].replace("contains", "").replace(")", "").split(",").map { inp -> inp.trim() }
        ingredientsMap[ingredients] = allergens.toMutableList()
    }
    return ingredientsMap
}

