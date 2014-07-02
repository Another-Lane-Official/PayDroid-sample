package jp.sample.sample_application;

import jp.alij.paydroid.common.Consts;
import jp.alij.paydroid.common.CustomerChange;
import jp.alij.paydroid.common.InputStatus;
import jp.alij.paydroid.common.QuickChargeStatus;
import jp.alij.paydroid.data.CustomerChangeCallback;
import jp.alij.paydroid.data.TransactionRequest;
import jp.alij.paydroid.data.TransactionResult;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * @author canu johann
 * �A�i�U�[���[���i���j
 * http://www.alij.ne.jp
 *
 */
public class MainActivity extends Activity {

	public SettlementReceiver receiver ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setReceiver();
    }
    
    /* �N���b�Nlistener */
	public void click(View v){
    	
    	//�V�K�g�����U�N�V����
		TransactionRequest tr = new TransactionRequest();
				
    	switch(v.getId()){
    	
    	case R.id.normal_payment:
    	
    		//�K�{�p�����[�^
    		tr.setAmount(210);
    		tr.setSiteId("99999928");
    		tr.setSitePass("qYhWsRLH");
    		
			//�C�Ӄp�����[�^
    		//�_��ɍ��킹�ĕ\���E��\����ݒ肵�Ă�������
    		//�ݒ肵�Ȃ��ꍇ��false�ɂȂ�܂��i��\���j
			tr.setVisibility_mail(InputStatus.INPUT_STATUS_MANDATORY);
			tr.setVisibility_adr1(InputStatus.INPUT_STATUS_OPTIONAL);
			tr.setVisibility_capital(InputStatus.INPUT_STATUS_OPTIONAL);
			
	    	//SDK���Ăяo��
			Intent i = new Intent(MainActivity.this, jp.alij.paydroid.activities.PaymentActivity.class);		
			i.putExtra(Consts.TRANSACTION_INDENT, tr);
			startActivity(i);
    		
    		break;
    		
    		
       	case R.id.quick_charge:
        	
    		//�K�{�p�����[�^
    		tr.setAmount(210);
    		tr.setSiteId("99999928");
    		tr.setSitePass("qYhWsRLH");
    		
			//�C�Ӄp�����[�^
    		//�_��ɍ��킹�ĕ\���E��\����ݒ肵�Ă�������
    		//�ݒ肵�Ȃ��ꍇ��false�ɂȂ�܂��i��\���j
			tr.setVisibility_mail(InputStatus.INPUT_STATUS_MANDATORY);
			tr.setVisibility_adr1(InputStatus.INPUT_STATUS_OPTIONAL);
			tr.setVisibility_capital(InputStatus.INPUT_STATUS_OPTIONAL);
			
			//�_��ɍ��킹��, id��pass(�������̓��[��)��n��
    		tr.setCustomerId("xxx");
    		tr.setCustomerPass("fsdf"); //customerPass��CustomerMail
			
	    	//SDK���Ăяo��
			Intent b = new Intent(MainActivity.this, jp.alij.paydroid.activities.PaymentActivity.class);		
			b.putExtra(Consts.TRANSACTION_INDENT, tr);
			startActivity(b);
    		
    		break;
    	   		
    	case R.id.quick_charge_second:
    		
    		//�K�{�p�����[�^
    		tr.setAmount(210);
    		tr.setSiteId("99999928");
    		tr.setSitePass("qYhWsRLH");    		
    		
    		//�_��ɍ��킹��, id��pass(�������̓��[��)��n��
    		tr.setCustomerId("xxx");
    		tr.setCustomerPass("fsdf");
    		
    		//�N�C�b�N�`���[�W�t���b�O
    		tr.setQuickChargeStatus(QuickChargeStatus.QUICK_CHARGE_SECOND_TIME_AND_MORE);
    		
        	//SDK���Ăяo��
    		Intent n = new Intent(MainActivity.this, jp.alij.paydroid.activities.PaymentActivity.class);		
    		n.putExtra(Consts.TRANSACTION_INDENT, tr);
    		startActivity(n);
    		
    		break;
    		
    	case R.id.quick_charge_change:
    		
    		tr.setSiteId("99999928");
    		tr.setSitePass("qYhWsRLH"); 
    		
    		tr.setCustomerId("xxx");
    		tr.setCustomerPass("fsdf");
    		
    		tr.setCardName("test");
    		tr.setCardNo("44444444444444444");
    		tr.setCardMonth("12");
    		tr.setCardYear("18");
    		
    		CustomerChange customerChange = new CustomerChange(this, tr, new CustomerChangeCallback() {
				@Override
				public void onCustomerChange(TransactionResult tr) {
					if(tr!= null && tr.getState()== Consts.RESPONSE_STATE_DATA_SUCCESS)
					Toast.makeText(getApplicationContext(), "�ڋq���̕ύX�͊����ɂȂ�܂����B", Toast.LENGTH_LONG).show();
					else
					Toast.makeText(getApplicationContext(), tr.getMsg(), Toast.LENGTH_LONG).show();
				}
			});
    		customerChange.changeInfo();    		
    		break;    		
    	}    	
    }
    
	/* ReceiverBroadcast��ݒ�*/
    private void setReceiver() {
        IntentFilter filter = new IntentFilter(Consts.RESPONSE_PAYMENT);
        receiver = new SettlementReceiver();
        registerReceiver(receiver, filter);
    }
        
    /*
     * ���ς������������܂��́u�߂�v�{�^���������Ƃ���Broadcast���z�M����
     * ���Lreceiver�������擾���A�f�[�^���������܂��B
     */
    private class SettlementReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			
			TransactionResult result = arg1.getExtras().getParcelable(Consts.RESPONSE_DATA); 
			
			switch (result.getState()){
			case Consts.RESPONSE_STATE_DATA_SUCCESS:	//���ϐ���
				Toast.makeText(getApplicationContext(), "���ς͊������܂����B����ID�F" + result.getTransactionId(), Toast.LENGTH_LONG).show();
				break;				
			case Consts.RESPONSE_STATE_DATA_CANCEL:	//���ϒ��~
				Toast.makeText(getApplicationContext(), "���ς����~����܂����B" , Toast.LENGTH_LONG).show();
				break;
			}			
		}
	}

}
