package net.starly.core.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.security.Key;
import java.util.Base64;

public class LicenseUtil {
    public static boolean checkLicense(JavaPlugin plugin, String licenseKey) {
        try {
            final Boolean[] result = {null};
            final int[] received = {0};
            final Key[] aesKey = new Key[1];
            final Key[] rsaPublicKey = new Key[1];

            WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://starly.kr:84")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    send("CHECK_CONNECTION_1");
                }

                @Override
                public void onMessage(String message) {
                    received[0]++;
                    if (result[0] != null && !result[0]) return;

                    if (received[0] == 1) {
                        if (!message.equals("CHECK_CONNECTION_2")) {
                            if (message.equals("BLACK_LISTED")) {
                                System.out.println("§8[§6" + plugin.getName() + "§8] §c해당 IP는 블랙리스트에 등록되어 있습니다. 관리자에게 문의하세요.");
                            }

                            result[0] = false;
                            close();
                        } else {
                            send(Base64.getEncoder().encodeToString("GIVE_ME_AES_KEY".getBytes()));
                        }
                    } else if (received[0] == 2) {
                        try {
                            aesKey[0] = new SecretKeySpec(Base64.getDecoder().decode(message.getBytes()), 0, Base64.getDecoder().decode(message.getBytes()).length, "AES");
                        } catch (Exception e) {
                            System.out.println("§8[§6" + plugin.getName() + "§8] §c암호화 과정에서 오류가 발생했습니다.");
                            e.printStackTrace();
                            result[0] = false;
                            close();
                            return;
                        }

                        send(Base64.getEncoder().encodeToString("GIVE_ME_RSA_PUBLIC_KEY".getBytes()));
                    } else if (received[0] == 3) {
                        try {
                            rsaPublicKey[0] = new SecretKeySpec(message.getBytes(), 0, message.getBytes().length, "RSA");
                        } catch (Exception e) {
                            System.out.println("§8[§6" + plugin.getName() + "§8] §c암호화 과정에서 오류가 발생했습니다.");
                            e.printStackTrace();
                            result[0] = false;
                            close();
                            return;
                        }

                        send(Base64.getEncoder().encodeToString(("CHECK_KEY_" + new String(aesKey[0].getEncoded()) + ":" + new String(Base64.getDecoder().decode(rsaPublicKey[0].getEncoded()))).getBytes()));
                    } else if (received[0] == 4) {
                        if (!message.equals("SUCCESS")) {
                            result[0] = false;
                            close();
                        } else {
                            try {
                                send(Base64.getEncoder().encodeToString(("CHECK_LICENSE_" + licenseKey).getBytes()));
                            } catch (Exception e) {
                                e.printStackTrace();
                                result[0] = false;
                                close();
                            }
                        }
                    } else if (received[0] == 5) {
                        if (message.equals("SUCCESS")) {
                            send(Base64.getEncoder().encodeToString(("CHECK_PLUGIN_" + plugin.getName()).getBytes()));
                        } else {
                            if (message.equals("LICENSE_NOT_FOUND")) {
                                System.out.println("§8[§6" + plugin.getName() + "§8] §c라이선스가 존재하지 않습니다.");

                                result[0] = false;
                                close();
                            } else if (message.equals("LICENSE_NOT_ACTIVE")) {
                                System.out.println("§8[§6" + plugin.getName() + "§8] §c라이선스가 만료 되었습니다.");

                                result[0] = false;
                                close();
                            } else if (message.equals("IP_NOT_MATCH")) {
                                System.out.println("§8[§6" + plugin.getName() + "§8] §c라이선스 IP가 일치하지 않아 해당 IP에서의 접속이 차단되었습니다. 관리자에게 문의하세요.");

                                result[0] = false;
                                close();
                            } else {
                                System.out.println("§8[§6" + plugin.getName() + "§8] §c알 수 없는 오류가 발생했습니다.");

                                result[0] = false;
                                close();
                            }
                        }
                    } else if (received[0] == 6) {
                        if (message.equals("SUCCESS")) {
                            System.out.println("§8[§6" + plugin.getName() + "§8] §a라이선스 검증을 완료했습니다. 플러그인을 실행합니다.");

                            result[0] = true;
                            close();
                        } else {
                            System.out.println("§8[§6" + plugin.getName() + "§8] §c라이선스가 일치하지 않습니다.");

                            result[0] = false;
                            close();
                        }
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    if (received[0] != 6) result[0] = false;
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                    result[0] = false;
                }
            };

            try {
                webSocketClient.connect();
            } catch (Exception e) {
                System.out.println("§8[§6" + plugin.getName() + "§8] §c서버와 연결할 수 없습니다.");
                result[0] = false;
            }

            while (result[0] == null) {
                Thread.sleep(10);
            }

            if (result[0] == false) {
                Bukkit.getPluginManager().disablePlugin(plugin);
                System.out.println("§8[§6" + plugin.getName() + "§8] §c플러그인을 종료합니다.");
            }
            return result[0];
        } catch (Exception e) {
            System.out.println("§8[§6" + plugin.getName() + "§8] §c오류가 발생했습니다.");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }
    }
}
