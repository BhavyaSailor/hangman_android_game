package com.example.hangman

data class WordEntry(
    val level: Int,
    val word: String,
    val category: String,
    val difficulty: String
)

object WordBank {

    val words = listOf(
        // Levels 1–10
    WordEntry(1, "APPLE", "Fruit", "Easy"),
    WordEntry(2, "ELEPHANT", "Animal", "Medium"),
    WordEntry(3, "CAR", "Vehicle", "Easy"),
    WordEntry(4, "ASTRONAUT", "Profession","Hard"),
    WordEntry(5, "CHAIR", "Furniture", "Easy"),
    WordEntry(6, "PHILOSOPHY", "Concept", "Impossible"),
    WordEntry(7, "BANANA", "Fruit", "Medium"),
    WordEntry(8, "DOG", "Animal", "Easy"),
    WordEntry(9, "MICROSCOPE", "Technology","Hard"),
    WordEntry(10, "MOUNTAIN", "Nature", "Medium"),

    // Levels 11–20
    WordEntry(11, "CAT", "Animal", "Easy"),
    WordEntry(12, "VOLCANO", "Nature", "Hard"),
    WordEntry(13, "TREE", "Nature", "Easy"),
    WordEntry(14, "COMPUTER", "Technology", "Medium"),
    WordEntry(15, "PLANE", "Vehicle", "Easy"),
    WordEntry(16, "ELECTROMAGNETISM", "Science", "Impossible"),
    WordEntry(17, "TEACHER", "Profession", "Medium"),
    WordEntry(18, "BREAD", "Food", "Easy"),
    WordEntry(19, "DICTIONARY", "Object", "Hard"),
    WordEntry(20, "FLOWER", "Nature", "Medium"),

    // Levels 21–30
    WordEntry(21, "BALL", "Toy", "Easy"),
    WordEntry(22, "GIRAFFE", "Animal", "Medium"),
    WordEntry(23, "NOTEBOOK", "Object", "Medium"),
    WordEntry(24, "LIGHTHOUSE", "Object", "Hard"),
    WordEntry(25, "SUN", "Nature", "Easy"),
    WordEntry(26, "HIPPOPOTAMUS", "Animal", "Impossible"),
    WordEntry(27, "MILK", "Food", "Easy"),
    WordEntry(28, "NEWSPAPER", "Object", "Medium"),
    WordEntry(29, "ALGORITHM", "Technology", "Hard"),
    WordEntry(30, "BIRD", "Animal", "Easy"),

    // Levels 31–40
    WordEntry(31, "HOUSE", "Object", "Easy"),
    WordEntry(32, "UMBRELLA", "Object", "Medium"),
    WordEntry(33, "CROCODILE", "Animal", "Hard"),
    WordEntry(34, "RIVER", "Nature", "Easy"),
    WordEntry(35, "CHOCOLATE", "Food", "Medium"),
    WordEntry(36, "CONSTITUTION", "Concept", "Impossible"),
    WordEntry(37, "PEN", "Object", "Easy"),
    WordEntry(38, "BICYCLE", "Vehicle", "Medium"),
    WordEntry(39, "SCIENTIST", "Profession", "Hard"),
    WordEntry(40, "THEATER", "Place", "Easy"),

    // Levels 41–50
    WordEntry(41, "CAMOUFLAGE", "Pattern", "Impossible"),
    WordEntry(42, "JACKET", "Clothing", "Easy"),
    WordEntry(43, "SANDWICH", "Food", "Medium"),
    WordEntry(44, "TELESCOPE", "Object", "Hard"),
    WordEntry(45, "RABBIT", "Animal", "Easy"),
    WordEntry(46, "QUANTUM", "Science", "Impossible"),
    WordEntry(47, "HOSPITAL", "Place", "Medium"),
    WordEntry(48, "TIGER", "Animal", "Easy"),
    WordEntry(49, "MOBILE", "Technology", "Medium"),
    WordEntry(50, "DIAMOND", "Object", "Hard"),
    // Levels 51–60
    WordEntry(51, "UNIVERSE", "Science", "Impossible"),
    WordEntry(52, "ARCHITECTURE", "Concept", "Hard"),
    WordEntry(53, "GRAVITY", "Science", "Hard"),
    WordEntry(54, "ENGINEER", "Profession", "Medium"),
    WordEntry(55, "COFFEE", "Beverage", "Easy"),
    WordEntry(56, "ASTROPHYSICS", "Science", "Impossible"),
    WordEntry(57, "BRIDGE", "Structure", "Medium"),
    WordEntry(58, "CLOCK", "Object", "Easy"),
    WordEntry(59, "CHEMISTRY", "Science", "Hard"),
    WordEntry(60, "INFINITY", "Concept", "Impossible"),

    // Levels 61–70
    WordEntry(61, "VACCINE", "Science", "Hard"),
    WordEntry(62, "ORCHESTRA", "Music", "Hard"),
    WordEntry(63, "MUSEUM", "Place", "Medium"),
    WordEntry(64, "OCEAN", "Nature", "Easy"),
    WordEntry(65, "NEUROSCIENCE", "Science", "Impossible"),
    WordEntry(66, "SOLDIER", "Profession", "Medium"),
    WordEntry(67, "COOKIE", "Food", "Easy"),
    WordEntry(68, "ASTEROID", "Space", "Hard"),
    WordEntry(69, "GALAXY", "Space", "Hard"),
    WordEntry(70, "SYMPHONY", "Music", "Impossible"),

    // Levels 71–80
    WordEntry(71, "MARATHON", "Sport", "Hard"),
    WordEntry(72, "RAINBOW", "Nature", "Easy"),
    WordEntry(73, "EVOLUTION", "Science", "Impossible"),
    WordEntry(74, "CAMPFIRE", "Outdoor", "Medium"),
    WordEntry(75, "ROBOTICS", "Technology", "Hard"),
    WordEntry(76, "LIBRARY", "Place", "Easy"),
    WordEntry(77, "DEMOCRACY", "Concept", "Impossible"),
    WordEntry(78, "ARTIST", "Profession", "Medium"),
    WordEntry(79, "ARCHAEOLOGY", "Historical", "Impossible"),
    WordEntry(80, "VOLLEYBALL", "Sport", "Medium"),

    // Levels 81–90
    WordEntry(81, "PYRAMID", "Structure", "Hard"),
    WordEntry(82, "CAMERA", "Technology", "Easy"),
    WordEntry(83, "ASTRONOMY", "Science", "Impossible"),
    WordEntry(84, "PIZZA", "Food", "Easy"),
    WordEntry(85, "SURGERY", "Medical", "Medium"),
    WordEntry(86, "PSYCHOLOGY", "Concept", "Hard"),
    WordEntry(87, "NEBULA", "Space", "Impossible"),
    WordEntry(88, "PAINTER", "Profession", "Medium"),
    WordEntry(89, "CORAL", "Nature", "Hard"),
    WordEntry(90, "SATELLITE", "Technology", "Hard"),

    // Levels 91–100
    WordEntry(91, "BLACKHOLE", "Space", "Hard"),
    WordEntry(92, "MAGNETISM", "Science", "Hard"),
    WordEntry(93, "ECLIPSE", "Space", "Medium"),
    WordEntry(94, "PLANET", "Space", "Easy"),
    WordEntry(95, "CRYPTOGRAPHY", "Technology", "Impossible"),
    WordEntry(96, "CLOUD", "Nature", "Easy"),
    WordEntry(97, "LANGUAGE", "Concept", "Medium"),
    WordEntry(98, "PHOTOGRAPHY", "Art", "Hard"),
    WordEntry(99, "CHEMICAL", "Science", "Medium"),
    WordEntry(100, "TELEVISION", "Technology", "Hard")
    )

    // 🔹 Get a word by level
//    fun getWordByLevel(level: Int): String {
//        return words.firstOrNull { it.level == level }?.word ?: ""
//
//    }
    fun getWordByLevel(level: Int): WordEntry {
        // Example: each level corresponds to an index in the list
        return words[(level - 1) % words.size]
    }

    // 🔹 Get a random word by difficulty
//    fun getWordByDifficulty(difficulty: String): WordEntry {
//        val filtered = words.filter { it.difficulty.equals(difficulty, ignoreCase = true) }
//        return filtered.random()
//    }

    fun getRandomWordEntry(): WordEntry {
        return words.random()
    }
}


