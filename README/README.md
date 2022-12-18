# ST-Core
- Starly Store의 코어는 Starly Store의 모든 기능을 제공합니다.
- 디스코드 : https://discord.gg/TF8jqSJjCG

---

## Configuration

---

### Config
* 간편하게 Config를 제작합니다.
```java
// net.starly.core.data.Config

public class Main {
    public static void main() {
        Config config = new Config("config");
        config.setString("test", "Hello");
        config.saveConfig();
    }
}
```

### MessageConfig
* 간편하게 MessageConfig를 제작합니다.
```java
// net.starly.core.data.MessageConfig

public class Main {
    public static void main() {
        Config config = new Config("message");
        config.loadDefaultConfig();
        Player player = Bukkit.getPlayer("Starly");
        MessageConfig messageConfig = new MessageConfig(config, "prefixPath");

        player.sendMessage(messageConfig.getMessage("errorMessage.wrong_command"));
        player.sendMessage(messageConfig.getMessage("errorMessage.wrong_command",
                Map.of("{player}", player.getName())));
    }
}
```

### Region
* 간편하게 Region을 제작합니다.
```java
// net.starly.core.data.Region

public class Main {
    public static void main() {
        Location loc1 = new Location(Bukkit.getWorld("world"), 0, 0, 0);
        Location loc2 = new Location(Bukkit.getWorld("world"), 0, 10, 0);
        Location loc3 = new Location(Bukkit.getWorld("world"), 0, 3, 0);
        Location loc4 = new Location(Bukkit.getWorld("world"), 3, 3, 0);

        Region region = new Region(loc1, loc2);
        System.out.println(region.getBlocks().size() + "개의 블럭이 구역안에 있습니다!");
        System.out.println(region.contains(loc3)); // -> true
        System.out.println(region.contains(loc4)); // -> false
        System.out.println(region.isInRegion(Bukkit.getPlayer("Starly"))); // -> true·false
    }
}
```
* 작동 예시  
  ![Region_1](./img/region_1.png)

### ItemBuilder
* 간편하게 아이템을 제작합니다.
```java
// net.starly.core.builder.ItemBuilder

public class Main {
    public static void main() {
        ItemStack itemStack = new ItemBuilder(Material.STONE)
                .setDisplayName("&r&e돌입니다!")
                .setAmount(1)
                .setLore("&r&e돌의 첫번째 로어입니다!", "&r&e돌의 두번째 로어입니다!")
                .setCustomModelData(1)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .build();
    }
}
```

### GUIBuilder
* 간편하게 GUI를 제작합니다.
```java
// net.starly.core.builder.GUIBuilder
// net.starly.core.builder.ItemBuilder

public class Main {
    public static void main() {
        GUIBuilder guiBuilder = new GUIBuilder("&e한줄 인벤토리입니다!", 1, null);
        guiBuilder.setItem(1, new ItemBuilder(Material.DIAMOND).setDisplayName("&e다이아몬드").setLore("&e다이아몬드입니다!"));
        guiBuilder.setItem(new ItemStack(Material.DIAMOND_BLOCK, 1), 2, 3, 4, 5, 6, 7, 8);
        guiBuilder.open(Bukkit.getPlayer("Starly"));
    }
}
```

### Tuple, Triple, Quadruple
* Tuple, Triple, Quadruple을 사용할 수 있습니다.
```java
// net.starly.core.data.util.Tuple
// net.starly.core.data.util.Triple
// net.starly.core.data.util.Quadruple

public class Main {
    public static void main() {
        Tuple<String, Integer> tuple = new Tuple<>("Hello", 1);
        Triple<String, Integer, Boolean> triple = new Triple<>("Hello", 1, true);
        Quadruple<String, Integer, Boolean, Double> quadruple = new Quadruple<>("Hello", 1, true, 1.0);
    }
}
```

### ParticleUtil
```java
// net.starly.core.util.ParticleUtil

public class Main {
    public static void main() {
        Location loc1 = new Location(Bukkit.getWorld("world"), 0, 0, 0);
        Location loc2 = new Location(Bukkit.getWorld("world"), 0, 10, 0);
        ParticleUtil.line(loc1, loc2, 1, Color.RED);
    }
}
```
* 작동 예시
![ParticleUtil_1](./img/particleutil_1.png)

### Time
```java
// net.starly.core.util.Time

public class Main {
    public static void main() {
        Time time = new Time(30);

        new BukkitRunnable() {
            @Override public void run() {
                time.subtract(1);
                
                if (time.getSeconds() == 0) {
                    System.out.println("시간이 끝났습니다!");
                    this.cancel();
                } else System.out.println(time.getSeconds() + "초 남았습니다!");
            }
        }.runTaskTimer(this, 0, 20);
    }
}
```
* 작동 예시  
![Time_1](./img/time_1.png)

### 