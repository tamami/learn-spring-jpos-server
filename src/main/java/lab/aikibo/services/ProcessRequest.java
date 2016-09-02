package lab.aikibo.services;

import lab.aikibo.App;

import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.iso.ISOException;
import org.jpos.iso.BaseChannel;

import java.io.IOException;

public class ProcessRequest implements ISORequestListener {
  public boolean process(ISOSource isoSrc, ISOMsg isoMsg) {
    try {
      App.getLogger().debug("Server menerima koneksi dari [" + ((BaseChannel)isoSrc).getSocket().getInetAddress().getHostAddress() + "]");
      if(isoMsg.getMTI().equalsIgnoreCase("1800")) {
        acceptNetworkMsg(isoSrc, isoMsg);
      }
    } catch(IOException ioe) {
      App.getLogger().debug(ioe);
    } catch(ISOException ex) {
      App.getLogger().debug(ex);
    }
    return false;
  }

  private void acceptNetworkMsg(ISOSource isoSrc, ISOMsg isoMsg) throws ISOException, IOException {
    App.getLogger().debug("Accepting Network Management Request");
    App.getLogger().debug("with MTI " + isoMsg.getMTI());
    ISOMsg reply = (ISOMsg) isoMsg.clone();
    reply.setMTI("1810");
    reply.set(39, "00");

    isoSrc.send(reply);
  }
}
