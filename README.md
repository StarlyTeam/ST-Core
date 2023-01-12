# ST-Core
- Starly Store의 코어는 Starly Store의 모든 기능을 제공합니다.
- 디스코드 : https://discord.gg/TF8jqSJjCG

---

## 1. Configuration

### 1-1. Config
* 간편하게 Config를 제작합니다.
```java
import net.starly.core.data.Config;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        // 사용법: new Config(fileName, pluginInstance);
        // ↪ fileName: ".yml"을 제외한 파일명.
        // ↪ pluginInstance: 플러그인 인스턴스.
        // ※ 플러그인 Resource에 포함된 파일이라면 자동으로 저장합니다.
        Config config = new Config("config", this);
        config.loadDefaultConfig();

        // 사용법: Config#setString(key, value);
        // ※ 값을 설정한 후, 자동으로 콘피그를 저장합니다.
        config.setString("test", "Hello");
        
        // 사용법: Config#saveConfig();
        // ※ Config를 저장합니다.
        config.saveConfig();
    }
}
```

### 1-2. MessageConfig (Deprecated)
* 간편하게 MessageConfig를 제작합니다.
```java
import net.starly.core.data.MessageConfig;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Config config = new Config("message");
        config.loadDefaultConfig();
        
        Player player = Bukkit.getPlayer("Starly");
        
        // 사용법: new MessageConfig(config, prefixPath);
        // ↪ config: Config 객체.
        // ↪ prefixPath: 메시지의 앞에 붙을 prefix의 Config내 경로. (옵션)
        MessageConfig messageConfig = new MessageConfig(config, "prefixPath");
        
        // 사용법: MessageConfig#getMessage(messagePath, replacements);
        // ↪ messagePath: 메시지의 Config내 경로.
        // ↪ replacements: 메시지에서 변경할 문자열의 목록. (옵션)
        player.sendMessage(messageConfig.getMessage("errorMessage.wrong_command",
                Map.of("{player}", player.getDisplayName(),
                        "{test}", "test")));
    }
}
```

### 1-3. MessageData (Deprecated)
* 간편하게 MessageConfig를 제작합니다.
* Field를 사용하여 Config를 불러오므로, 리로드를 별도로 하지 않아도 되어 더욱 편리합니다.
```java
import net.starly.core.data.MessageData;

public class Main extends JavaPlugin {
    public Config config;
    
    @Override
    public void onEnable() {
        config = new Config("message", this);
        config.loadDefaultConfig();
        
        config.setString("test", "testValue!");
        
        // 사용법: new MessageData(class, variableName);
        // ↪ class: Config 변수가 선언되어 있는 클래스.
        // ↪ variableName: Config 변수의 이름.
        MessageData messageData = new MessageData(this, "config");
        
        // 사용법: MessageData#getMessage(messagePath, replacements);
        // ↪ messagePath: 메시지의 Config내 경로.
        // ↪ replacements: 메시지에서 변경할 문자열의 목록. (옵션)
        System.out.println(messageData.getMessage("test",
                Map.of("{player}", player.getDisplayName(),
                        "{test}", "test")));
    }
}
```

### 1-4. ConfigSection
* Config의 특정 섹션을 관리합니다.
```java
import net.starly.core.data.ConfigSection;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Config config = new Config("config", this);
        config.loadDefaultConfig();
        
        // 사용법: Config#getSection(path);
        // ↪ path: Config의 경로.
        ConfigSection configSection = config.getSection("test");
        
        // 사용법: ConfigSection#getString(key);
        // ↪ key: Config의 키.
        System.out.println(configSection.getString("test"));
    }
}
```


## 2. Object

### 2-1. Region
* 간편하게 Region을 제작합니다.
```java
import net.starly.core.data.Region;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Location loc1 = new Location(Bukkit.getWorld("world"), 0, 0, 0);
        Location loc2 = new Location(Bukkit.getWorld("world"), 0, 10, 0);
        Location loc3 = new Location(Bukkit.getWorld("world"), 0, 3, 0);
        Location loc4 = new Location(Bukkit.getWorld("world"), 3, 3, 0);
        
        // 사용법: new Region(loc1, loc2);
        // ↪ loc1: 첫번째 위치.
        // ↪ loc2: 두번째 위치.
        Region region = new Region(loc1, loc2);
        
        // 사용법: Region#getBlocks();
        // ※ 구역에 포함된 모든 블럭을 반환합니다. (일시적 부하가 발생할 수 있습니다.)
        System.out.println(region.getBlocks().size() + "개의 블럭이 구역안에 있습니다!");
        
        // 사용법: Region#contains(loc);
        // ↪ loc: 포함 여부를 확인할 위치.
        // ※ 구역에 포함된 위치인지 반환합니다.
        System.out.println(region.contains(loc3)); // -> true
        System.out.println(region.contains(loc4)); // -> false
      
        // 사용법: Region#isInRegion(player);
        // ↪ player: 포함 여부를 확인할 플레이어.
        // ※ 구역에 포함된 플레이어인지 반환합니다.
        System.out.println(region.isInRegion(Bukkit.getPlayer("Starly"))); // -> true|false
    }
}
```
* 작동 예시  
  ![Region_1](README/img/region_1.png)

### 2-2. Tuple, Triple, Quadruple
* Tuple, Triple, Quadruple 객체를 사용할 수 있습니다.
```java
import net.starly.core.data.util.Tuple;
import net.starly.core.data.util.Triple;
import net.starly.core.data.util.Quadruple;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        // 사용법: new Tuple<>(a, b);
        Tuple<String, Integer> tuple = new Tuple<>("Hello", 1);
        
        // 사용법: new Triple<>(a, b, c);
        Triple<String, Integer, Boolean> triple = new Triple<>("Hello", 1, true);
        
        // 사용법: new Quadruple<>(a, b, c, d);
        Quadruple<String, Integer, Boolean, Double> quadruple = new Quadruple<>("Hello", 1, true, 1.0);
    }
}
```

### 2-3. Time
```java
import net.starly.core.util.Time;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        // 사용법: new Time(time);
        // ↪ time: 시간. (단위: 초)
        Time time = new Time(30);

        new BukkitRunnable() {
            @Override public void run() {
                // 사용법: Time#subtract(time);
                // ↪ time: 뺄 시간. (단위: 초)
                time.subtract(1);
                
                // 사용법: Time#getSeconds();
                // ※ 남은 시간을 반환합니다. (단위: 초)
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
  ![Time_1](README/img/time_1.png)

### 2-4. Metrics
* Metrics를 사용할 수 있습니다.
```java
import net.starly.core.bstats.Metrics;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        // 사용법: new Metrics(this, id);
        // ↪ this: 플러그인 인스턴스.
        // ↪ id: 'bstats.org'에서 발급받은 플러그인 ID.
        Metrics metrics = new Metrics(this, 12345);
    }
}
```

  
## 3. Builder

### 3-1. ItemBuilder
* 간편하게 아이템을 제작합니다.
```java
import net.starly.core.builder.ItemBuilder;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        // 사용법: new ItemBuilder(Material); 등등...
        ItemStack itemStack = new ItemBuilder(Material.STONE)
                // 사용법: ItemBuilder#setDisplayName(displayName);
                // ↪ displayName: 아이템의 이름.
                .setDisplayName("&r&e돌입니다!")
                
                // 사용법: ItemBuilder#setLore(lore...);
                // ↪ lore...: 아이템의 설명.
                .setLore("&r&e돌의 첫번째 로어입니다!", "&r&e돌의 두번째 로어입니다!")
                
                // 사용법: ItemBuilder#setAmount(amount);
                // ↪ amount: 아이템의 개수.
                .setAmount(1)
                
                // 사용법: ItemBuilder#setCustomModelData(customModelData);
                // ↪ customModelData: 아이템의 모델 데이터.
                .setCustomModelData(1)
                
                // 사용법: ItemBuilder#addEnchantment(enchantment, level);
                // ↪ enchantment: 아이템에 추가할 강화.
                .addEnchantment(Enchantment.DURABILITY, 3)
                
                // 사용법: ItemBuilder#build();
                // ※ 아이템을 설정 완료합니다.
                .build();
    }
}
```


## 4. Util

### 4-1. ParticleUtil
```java
import net.starly.core.util.ParticleUtil;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Location loc1 = new Location(Bukkit.getWorld("world"), 0, 0, 0);
        Location loc2 = new Location(Bukkit.getWorld("world"), 0, 10, 0);
        
        // 사용법: ParticleUtil#line(loc1, loc2, space, color);
        // ↪ loc1: 시작 위치.
        // ↪ loc2: 끝 위치.
        // ↪ space: 파티클 사이의 간격. (단위: 블럭)
        // ↪ color: 파티클의 색상.
        // ※ loc1과 loc2을 잇는 선을 color의 색으로 생성합니다.
        ParticleUtil.line(loc1, loc2, 1, Color.RED);
    }
}
```
* 작동 예시  
![ParticleUtil_1](README/img/particleutil_1.png)

### 4-2. InventoryUtil
```java
import net.starly.core.util.InventoryUtil;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Player player = Bukkit.getPlayer("Starly");
        Inventory inventory = player.getInventory();
        
        // 사용법: InventoryUtil#hasEnoughSpace(slots);
        // ↪ inventory: 인벤토리.
        // ↪ slots: 아이템을 넣을 슬롯.
        // ※ 인벤토리에 slots만큼의 칸이 비어있는지 확인합니다.
        System.out.println(InventoryUtil.hasEnoughSpace(inventory, 3)); // true|false
      
        // 사용법: InventoryUtil#getSpace(inventory);
        // ↪ inventory: 인벤토리.
        // ※ 인벤토리에 비어있는 칸의 개수를 반환합니다.
        System.out.println(InventoryUtil.hasEnoughSpace(inventory));
    }
}
```