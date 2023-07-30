package com.example.edynamixapprenticeship.model.weather;

public class WeatherCodeDecoder {
    public static String getEmoji(int code){
        switch (code){
            case 1000: return "☀";
            case 1255: case 1213: case 1210: return "❄";
            case 1006: case 1009: return "☁";
            case 1273: case 1276: return "⛈";
            case 1114: case 1117: case 1289: return "\uD83C\uDF28";
            case 1153: case 1168: case 1171: return "\uD83C\uDF27";
            case 1150: case 1180: return "\uD83C\uDF26";
            case 1003: return "\uD83C\uDF25";
            case 1279: case 1087: return "\uD83C\uDF29";
            case 1030: case 1135: case 1147: return "\uD83C\uDF01";
        }
        if(code >= 1062 && code <= 1072) {
            return "\uD83C\uDF24";
        }
        if((code >= 1216 && code <= 1237) || (code >= 1255 && code <= 1264)) {
            return "\uD83C\uDF28";
        }
        if((code >= 1183 && code <= 1207) || (code >= 1240 && code <= 1252)) {
            return "\uD83C\uDF27";
        }
        return "Unexpected value";
    }
}
