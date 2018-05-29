function FindProxyForURL(url, host) {
    if (host.indexOf("usefullc.cn") != -1) {
        return "PROXY 192.168.0.237:8888";
    }
    return direct;
}