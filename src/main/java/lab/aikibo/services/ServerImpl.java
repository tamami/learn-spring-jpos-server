package lab.aikibo.services;

import lab.aikibo.App;

import org.jpos.iso.ISOPackager;
import org.jpos.iso.ServerChannel;
import org.jpos.iso.ISOServer;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.GenericPackager;

public class ServerImpl implements Server {

  @Override
  public void start() {
    String hostname = "localhost";
    int portNumber = 15243;

    try {
      ISOPackager packager = new GenericPackager("packager/iso93ascii.xml");
      ServerChannel channel = new ASCIIChannel(hostname, portNumber, packager);
      ISOServer server = new ISOServer(portNumber, channel, null);
      server.addISORequestListener(new ProcessRequest());

      new Thread(server).start();

      App.getLogger().debug("Server siap menerima koneksi pada port [" + portNumber + "]");
    } catch(ISOException isoe) {
      App.getLogger().debug(isoe);
    }
  }

  @Override
  public void start2() {
    try {
    ISOPackager packager = new GenericPackager("packager/iso93ascii.xml");
    ServerChannel channel = new ASCIIChannel("localhost", 15243, packager);

    ISOServer server = new ISOServer(8801, channel, null);



    server.addISORequestListener(new ISORequestListener() {
      int i = 1;
      public boolean process(ISOSource sender, ISOMsg req) {
        try {
          ISOMsg inqResp = (ISOMsg) req.clone();
          App.getLogger().debug(i + " - Ada Client Terhubung");

          sender.send(inqResp);
          i++;
          return true;
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        return false;
      }
    });

    new Thread(server).start();
    System.out.println("Server ready!");
  } catch(ISOException isoe) {
    App.getLogger().debug("Error: " + isoe);
  }
  }

}
