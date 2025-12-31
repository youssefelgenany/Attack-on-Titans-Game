# Attack on Titan: Utopia

A strategic tower-defense game built with Java and JavaFX, where you defend your walls against waves of Titans by strategically deploying weapons across multiple lanes.


## 🎮 Game Overview

**Attack on Titan: Utopia** is a turn-based tower defense game inspired by the Attack on Titan universe. Players must strategically purchase and deploy weapons to protect their walls from advancing Titans across multiple lanes. Each turn brings new challenges as Titans spawn and move closer to your defenses.

## ✨ Features

### Game Modes
- **Easy Mode**: 3 lanes with 250 resources per lane - Perfect for beginners
- **Hard Mode**: 5 lanes with 125 resources per lane - For experienced strategists

### Visual Design
- **Dystopian Theme**: Dark, industrial aesthetic with orange accent colors
- **Professional UI**: Modern, polished interface with smooth animations
- **Custom Styled Dialogs**: Beautiful lane selection and error windows
- **Dynamic Visuals**: Titans move realistically down lanes with unique positioning

### Game Mechanics
- **Turn-Based Gameplay**: Strategic planning with turn-by-turn progression
- **Multiple Weapon Types**: 4 distinct weapons with unique damage and costs
- **Titan Variety**: 4 different Titan classes with unique behaviors
- **Resource Management**: Earn and spend resources wisely
- **Danger System**: Lanes have dynamic danger levels based on Titan presence
- **Battle Phases**: Game difficulty increases through EARLY → INTENSE → GRUMBLING phases

### Weapons
1. **Anti Titan Shell** (Piercing Cannon)
   - Type: PiercingCannon
   - Price: 25 resources
   - Damage: 10 damage

2. **Long Range Spear** (Sniper Cannon)
   - Type: SniperCannon
   - Price: 25 resources
   - Damage: 35 damage

3. **Wall Spread Cannon** (Volley Spread Cannon)
   - Type: VolleySpreadCannon
   - Price: 100 resources
   - Damage: 5 damage

4. **Proximity Trap** (Wall Trap)
   - Type: WallTrap
   - Price: Varies
   - Damage: Varies

### Titan Types
- **Pure Titan**: Basic enemy unit
- **Abnormal Titan**: Faster and more unpredictable
- **Armored Titan**: Heavily armored, harder to defeat
- **Colossal Titan**: Massive and powerful, accelerates as it moves

## 🚀 Getting Started

### Prerequisites
- **Java Development Kit (JDK)**: Version 11 or higher
- **JavaFX**: Included in the project dependencies
- **IDE**: IntelliJ IDEA, Eclipse, or any Java-compatible IDE

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/Attack-on-Titans-Game.git
   cd Attack-on-Titans-Game
   ```

2. **Open in your IDE**
   - Import the project as a Java project
   - Ensure JavaFX is properly configured

3. **Run the game**
   - Navigate to `src/game/gui/gameGui.java`
   - Run the `main` method or execute the `gameGui` class

### Building the Project

The project uses JavaFX modules. Make sure your IDE is configured to:
- Include JavaFX libraries
- Set the module path correctly
- Include all image resources from the `src/` directory

## 🎯 How to Play

### Basic Gameplay

1. **Start the Game**
   - Choose between Easy Mode (3 lanes) or Hard Mode (5 lanes)
   - Read the instructions for detailed gameplay tips

2. **Deploy Weapons**
   - Click on a weapon in the Weapon Shop panel
   - Select the lane where you want to deploy it
   - Weapons are placed at the top of the lane (near the wall)

3. **Pass Turns**
   - Click "Pass Turn" to advance the game
   - Titans will move closer to your walls
   - Your weapons will automatically attack Titans
   - You'll earn resources from defeated Titans

4. **Monitor Your Defenses**
   - Watch the danger level of each lane
   - Keep an eye on wall HP - if it reaches 0, the lane is lost
   - Manage your resources wisely

### Strategy Tips

- **Balance Your Defenses**: Don't focus all weapons on one lane
- **Watch Danger Levels**: Lanes with higher danger need more attention
- **Resource Planning**: Save resources for powerful weapons when needed
- **Titan Patterns**: Learn which Titans appear in each phase
- **Wall Health**: Monitor wall HP closely - it's your last line of defense

## 🎨 User Interface

### Main Game Screen
- **Left Side**: Game lanes showing Titans, weapons, and lane information
- **Right Side**: Battle status panel and weapon shop
- **Bottom**: Control buttons (Pass Turn, Restart)

### Lane Information
Each lane displays:
- Lane number
- Current danger level
- Wall HP (health points)

### Battle Status Panel
- **Phase**: Current battle phase (Early, Intense, Grumbling)
- **Score**: Your current score
- **Turn**: Current turn number
- **Resources**: Available resources (highlighted in orange)

### Weapon Shop
- Displays all available weapons with:
  - Weapon name (in orange, bold)
  - Type
  - Price
  - Damage

## 🏗️ Project Structure

```
Attack-on-Titans-Game/
├── src/
│   ├── game/
│   │   ├── engine/
│   │   │   ├── Battle.java          # Main game logic
│   │   │   ├── BattlePhase.java    # Game phases
│   │   │   ├── lanes/
│   │   │   │   └── Lane.java       # Lane management
│   │   │   ├── titans/
│   │   │   │   ├── Titan.java      # Base Titan class
│   │   │   │   ├── PureTitan.java
│   │   │   │   ├── AbnormalTitan.java
│   │   │   │   ├── ArmoredTitan.java
│   │   │   │   └── ColossalTitan.java
│   │   │   └── weapons/
│   │   │       ├── Weapon.java     # Base Weapon class
│   │   │       ├── PiercingCannon.java
│   │   │       ├── SniperCannon.java
│   │   │       ├── VolleySpreadCannon.java
│   │   │       └── WallTrap.java
│   │   └── gui/
│   │       └── gameGui.java        # Main GUI application
│   ├── icon.png                     # Game icon
│   ├── start_bg.png                 # Start screen background
│   ├── main_bg.png                  # Main game background
│   └── lane_bg.png                  # Lane background
├── titans.csv                        # Titan data
├── weapons.csv                       # Weapon data
└── README.md                         # This file
```

## 🔧 Technical Details

### Technologies Used
- **Java**: Core programming language
- **JavaFX**: GUI framework
- **CSV Files**: Data storage for Titans and Weapons

### Key Features Implementation
- **Image Caching**: Optimized performance with HashMap-based image caching
- **Exception Handling**: Robust error handling throughout the application
- **Dynamic UI**: Responsive layouts that adapt to different screen sizes
- **Position Tracking**: Titans maintain their horizontal position as they move
- **Resource Management**: Efficient resource tracking and spending

### Game Phases
- **EARLY** (Turns 1-14): Introduction phase with basic Titans
- **INTENSE** (Turns 15-29): Increased difficulty and Titan variety
- **GRUMBLING** (Turn 30+): Maximum difficulty with exponential Titan spawning

## 🎮 Controls

- **Mouse**: Click buttons and select options
- **Weapon Selection**: Click on weapon cards in the shop
- **Lane Selection**: Click on lane buttons in the selection dialog
- **Pass Turn**: Click "Pass Turn" button to advance
- **Restart**: Click "Restart Game" to return to the start screen

## 🐛 Known Issues

- None currently reported

## 📝 License

This project is created for educational purposes.

## 👨‍💻 Development

### Contributing
Feel free to fork this project and submit pull requests for any improvements!

### Future Enhancements
- Additional weapon types
- More Titan varieties
- Sound effects and music
- Save/Load game functionality
- Leaderboard system
- Achievement system

## 🙏 Acknowledgments

- Inspired by the Attack on Titan anime/manga series
- Built with JavaFX for modern UI design
- Game mechanics inspired by classic tower defense games

## 📧 Contact

For questions or suggestions, please open an issue on the GitHub repository.

---

**Defend your walls. Survive the Titans. Good luck, Commander!** 🛡️
