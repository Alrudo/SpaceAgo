# SpaceAgo Game

Goal of our project is to create some sort of _shoot 'em up_ game, which action take place in space and where player is controlling spaceship and killing enemies, while trying not to die and collect as much score as possible. 

## Getting Started

First of all clone this project to your computer and open it via Intellij or any other development environment you are using.

After project was cloned and gradle changes imported, you can run DesktopLauncher, that is located in `desktop` module, to launch game

### Building game

To build launchable jar file you need to type

```
gradle dist:desktop
```

This will generate jar file that contains all required libraries and dependencies 

You can find file in `build` folder of `desktop` module

## Developing

There are a lot of neat features to make developing process easier and getting more stable result

### Debug mode

For being able to see more logs and overall information about game world you can enable debug mode by changing value of debug variable in config to true

Game config is located at `core\src\org\nullpointerid\spaceago\config`

In enabled mode you will see game scene grid, entity hitboxes and much more  

## Built With

* [LibGDX](https://libgdx.badlogicgames.com/) - The game framework used
* [Gradle](https://gradle.org/) - Dependency Management

## Authors

* **Kevin Raja** - *Initial work, menu creator, sound artist* - [keraja](https://gitlab.cs.ttu.ee/keraja)
* **Aleksander Rudoi** - *Innovator, game features developer* - [alrudo](https://gitlab.cs.ttu.ee/alrudo)
* **Sergei Saal** - *Leader, networking, some AI* - [sesaal](https://gitlab.cs.ttu.ee/sesaal)

## License

This project is licensed under [GNU General public license](https://www.gnu.org/licenses/gpl-3.0.html)

