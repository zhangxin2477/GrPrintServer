package com.printserver.base;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.widget.EditText;

import java.io.IOException;

/**
 * Created by zhangxin on 2015/9/8.
 */
public class BaseNfc  {

    /*
    为了凑够32bit，如果只有3个字节的卡号参加防冲突循环，则卡片自动在3个卡号字节之前增加一个字节0x88,这个0x88被称为层标签(cascade tag)，从而满足防冲突循环对32bit卡号的要求
     */

    private static byte password[] = { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
            (byte) 0xff, (byte) 0xff, (byte) 0xff };

    public static String ScanNfc(Context context,Intent intent){
        boolean auth = false;
        String cardHeadStr ="";
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        cardHeadStr ="ID："+ BaseHelp.bytesToHexString(tag.getId());
        String[] techList = tag.getTechList();
        boolean isMifareClassic = false;
        cardHeadStr +="\r\nTECH：";
        for (String tech : techList) {
            cardHeadStr +=tech+",";
            if (tech.indexOf("MifareClassic") >= 0) {
                isMifareClassic = true;
                break;
            }
        }
        if (!isMifareClassic) {
            BaseHelp.ShowDialog(context, "您的卡不支持！", 1);
            return null;
        }
        MifareClassic mfc = MifareClassic.get(tag);
        try {
            String cardContentStr = "";
            mfc.connect();
            int type = mfc.getType();// 获取TAG的类型
            int sectorCount = mfc.getSectorCount();// 获取TAG中包含的扇区数
            int blockCount=mfc.getBlockCount();//获取TGA中包含的块数
            int memoryCount=mfc.getSize();//获取存储空间
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }

            byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            int before = (int) Long.parseLong(BaseHelp.bytesToHexString(myNFCID), 16);
            int r24 = before >> 24 & 0x000000FF;
            int r8 = before >> 8 & 0x0000FF00;
            int l8 = before << 8 & 0x00FF0000;
            int l24 = before << 24 & 0xFF000000;

            cardContentStr += "ID(10):" + Long.parseLong(Integer.toHexString((r24 | r8 | l8 | l24)), 16)
                    + "\nID(16):" + BaseHelp.bytesToHexString2(myNFCID)
                    + "\nType:" + typeS
                    + "\nSector:" + sectorCount
                    + "\n Block:" + blockCount
                    + "\nSize:" + memoryCount + "B";

            sectorCount=1;
            String tmp=null;
            for (int j = 0; j < sectorCount; j++) {
                auth = mfc.authenticateSectorWithKeyA(j,password);
                int bCount;
                int bIndex;
                if (auth) {
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    bCount=1;
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
                        cardContentStr+="\nSector0:"+ BaseHelp.bytesToHexString(data);
                        //message.setText("88"+BaseHelp.bytesToHexString(data).substring(0,6));
                        tmp="88"+BaseHelp.bytesToHexString(data).substring(0,6);
                        bIndex++;
                        break;
                    }
                } else {
                    BaseHelp.ShowDialog(context, "无法读取卡数据，卡密码错误！", 1);
                    return null;
                }
                break;
            }
            return  tmp;
            //message.setText(cardContentStr);
        } catch (Exception e) {
            e.printStackTrace();
            BaseHelp.ShowDialog(context, "由于您刷卡太快，卡数据未读取完整！", 1);
        } finally {
            try {
                if (mfc != null) {
                    mfc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
