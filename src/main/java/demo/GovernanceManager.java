package demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.github.ontio.OntSdk;
import com.github.ontio.account.Account;
import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.github.ontio.crypto.SignatureScheme;
import com.github.ontio.sdk.wallet.Wallet;
import config.Config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class GovernanceManager {
    public static void main(String[] args) throws Exception {

        InputStream inputStream = new FileInputStream("govrnanceConfig.json");
        String text = IOUtils.toString(inputStream);
        Config config = JSON.parseObject(text, Config.class);
        if(config.getWallets().size() != config.getPasswords().size()){
            System.out.println("wallets length does not equals password length");
            return;
        }
        OntSdk ontSdk = OntSdk.getInstance();
        if(config.getDefaultPort()==null||config.getDefaultPort().equals("")){
            ontSdk.setRestful(config.getUrl()+":"+config.getDefaultPort());
        }else{
            if(config.defaultPort.equals("restPort")){
                ontSdk.setRestful(config.url+":"+config.restPort);
            }else if(config.defaultPort.equals("rpcPort")){
                ontSdk.setRpc(config.url+ ":" + config.rpcPort);
            }
        }

        Account[] accounts = new Account[config.wallets.size()];
        for(int i=0;i<accounts.length;i++) {
            inputStream = new FileInputStream(config.wallets.get(i));
            text = IOUtils.toString(inputStream);
            Wallet wallet = JSON.parseObject(text, Wallet.class);
            com.github.ontio.sdk.wallet.Account account = wallet.getAccounts().get(0);
            accounts[i] = getAccount(account.key,config.passwords.get(i),account.address,account.getSalt(),SignatureScheme.fromScheme(account.signatureScheme));
        }
        if(accounts.length == 1) {
            String txhash = ontSdk.nativevm().governance().approveCandidate(accounts[0],config.peerPublicKey,accounts[0],ontSdk.DEFAULT_GAS_LIMIT,config.gasprice);
            Thread.sleep(6000);
            common.waitResult(ontSdk,txhash);
        }else {
            byte[][] pubkeys = new byte[accounts.length][];
            for(int i=0;i<accounts.length;i++){
                pubkeys[i] = accounts[i].serializePublicKey();
            }
            Address multiAddr = Address.addressFromMultiPubKeys(config.M,pubkeys);
            Account[] tempAcc = new Account[config.M];
            System.arraycopy(accounts,0,tempAcc,0,config.M);
            byte[][] tempPubkey = new byte[pubkeys.length-config.M][];
            System.arraycopy(pubkeys,config.M,tempPubkey,0,tempPubkey.length);
            String txhash = ontSdk.nativevm().governance().approveCandidate(multiAddr,config.M,tempAcc,tempPubkey,config.peerPublicKey,accounts[0],ontSdk.DEFAULT_DEPLOY_GAS_LIMIT,config.gasprice);
            Thread.sleep(6000);
            common.waitResult(ontSdk,txhash);
        }
    }

    public static Account getAccount(String enpri,String password,String address,byte[] salt,SignatureScheme scheme) throws Exception {
        String privateKey = Account.getGcmDecodedPrivateKey(enpri,password,address,salt,16384,scheme);
        Account account = new Account(Helper.hexToBytes(privateKey),SignatureScheme.SHA256WITHECDSA);
        return account;
    }
}
