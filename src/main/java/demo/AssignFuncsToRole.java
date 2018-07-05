package demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;
import com.github.ontio.OntSdk;
import com.github.ontio.account.Account;
import com.github.ontio.common.Common;
import com.github.ontio.common.Helper;
import com.github.ontio.crypto.SignatureScheme;
import com.github.ontio.sdk.wallet.Identity;
import com.github.ontio.sdk.wallet.Wallet;
import config.AssignAuthConfig;
import config.AssignRoleConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class AssignFuncsToRole {
    public static void main(String[] args) throws Exception {
        File directory = new File("");//参数为空
        String path = directory.getCanonicalPath();
        path = path.replace("src"+File.separator+ "main"+File.separator + "java","");
//        System.out.println(path);
        InputStream inputStream = new FileInputStream(path+File.separator+"assignRoleConfig.json");
        String text = IOUtils.toString(inputStream);
        AssignRoleConfig config = JSON.parseObject(text, AssignRoleConfig.class);
        if(config.wallets.size() != config.passwords.size()){
            System.out.println("wallets length does not equals password length");
            return;
        }
        OntSdk ontSdk = OntSdk.getInstance();
        if(config.defaultPort==null||config.defaultPort.equals("")){
            if(config.restPort == null ||config.restPort.equals("")){
                System.out.println("please select rest or rpc connect");
                return;
            }
            ontSdk.setRestful(config.url+":"+config.restPort);
        }else{
            if(config.defaultPort.equals("restPort")){
                ontSdk.setRestful(config.url+":"+config.restPort);
            }else if(config.defaultPort.equals("rpcPort")){
                ontSdk.setRpc(config.url+ ":" + config.rpcPort);
            }
        }
        if(config.wallets == null || config.wallets.size() == 0){
            System.out.println("please set admin ontId and payer wallet");
            return;
        }
        if(config.wallets.size() == 1) {
            ontSdk.openWalletFile(config.wallets.get(0));
            Identity adminOntId = ontSdk.getWalletMgr().getDefaultIdentity();
            com.github.ontio.sdk.wallet.Account account = ontSdk.getWalletMgr().getDefaultAccount();
            Account payerAcc = getAccount(account.key,config.passwords.get(1),account.address,
                    account.getSalt(),SignatureScheme.fromScheme(account.signatureScheme));
            String txhash = ontSdk.nativevm().auth().assignFuncsToRole(adminOntId.ontid,config.passwords.get(0),adminOntId.controls.get(0).getSalt(),1,config.contractAddress,config.role,
                    new String[]{config.functionName},payerAcc,config.gaslimit,config.gasprice);
            Thread.sleep(6000);
            common.waitResult(ontSdk,txhash);
            return;
        }
        ontSdk.openWalletFile(config.wallets.get(0));
        Identity adminOntId;
        if(ontSdk.getWalletMgr().getWallet().getIdentities().size() != 0){
            adminOntId = ontSdk.getWalletMgr().getWallet().getIdentities().get(0);
        }else {
            com.github.ontio.sdk.wallet.Account account = ontSdk.getWalletMgr().getDefaultAccount();
            adminOntId = ontSdk.getWalletMgr().importIdentity(account.key,config.passwords.get(0),account.getSalt(),account.address);
        }

        ontSdk.getWalletMgr().getWallet().clearAccount();
        ontSdk.getWalletMgr().getWallet().clearIdentity();

        ontSdk.openWalletFile(config.wallets.get(1));

        com.github.ontio.sdk.wallet.Account account = ontSdk.getWalletMgr().getDefaultAccount();
        Account payerAcc = getAccount(account.key,config.passwords.get(1),account.address,
                account.getSalt(),SignatureScheme.fromScheme(account.signatureScheme));

        ontSdk.getWalletMgr().importIdentity(adminOntId.controls.get(0).key,config.passwords.get(0),adminOntId.controls.get(0).getSalt(),adminOntId.ontid.replace(Common.didont,""));
        String txhash = ontSdk.nativevm().auth().assignFuncsToRole(adminOntId.ontid,config.passwords.get(0),adminOntId.controls.get(0).getSalt(),1,config.contractAddress,config.role,
                new String[]{config.functionName},payerAcc,ontSdk.DEFAULT_GAS_LIMIT,config.gasprice);
        Thread.sleep(6000);
        common.waitResult(ontSdk,txhash);
    }

    public static Account getAccount(String enpri,String password,String address,byte[] salt,SignatureScheme scheme) throws Exception {
        String privateKey = Account.getGcmDecodedPrivateKey(enpri,password,address,salt,16384,scheme);
        Account account = new Account(Helper.hexToBytes(privateKey),scheme);
        return account;
    }
}
