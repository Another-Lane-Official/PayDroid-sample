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
    		tr.setSiteId("your_site_id");
    		tr.setSitePass("your_site_password");
    		
    		//�A�C�e�����̕\���E��\��
    		tr.setItemId("�{�f�B���[�V���� 1L"); //itemId��ݒ肵�Ȃ��ꍇ�͕\������܂���

    		//���̓p�����[�^�̐ݒ�
    		//�f�t�H���g�Ŕ�\��
//    		tr.setVisibility_zip(InputStatus.INPUT_STATUS_OPTIONAL);
//    		tr.setVisibility_adr1(InputStatus.INPUT_STATUS_OPTIONAL);
//    		tr.setVisibility_adr2(InputStatus.INPUT_STATUS_OPTIONAL);
//    		tr.setVisibility_capital(InputStatus.INPUT_STATUS_OPTIONAL);
//    		tr.setVisibility_country(InputStatus.INPUT_STATUS_OPTIONAL);
//    		tr.setVisibility_mail(InputStatus.INPUT_STATUS_OPTIONAL);
//    		tr.setVisibility_name(InputStatus.INPUT_STATUS_OPTIONAL);
//    		tr.setVisibility_note(InputStatus.INPUT_STATUS_OPTIONAL);
    		tr.setVisibility_tel(InputStatus.INPUT_STATUS_OPTIONAL);
    		
    				
	    	//SDK���Ăяo��
			Intent i = new Intent(MainActivity.this, jp.alij.paydroid.activities.PaymentActivity.class);		
			i.putExtra(Consts.TRANSACTION_INDENT, tr);
			startActivity(i);
    		
    		break;
    		
    		
       	case R.id.quick_charge:
        	
    		//�K�{�p�����[�^
    		tr.setAmount(210);
    		tr.setSiteId("your_site_id");
    		tr.setSitePass("your_site_password");
    								
			//�_��ɍ��킹��, id��pass(�������̓��[��)��n��
    		tr.setCustomerId("your_customer_id");
    		tr.setCustomerPass("your_customer_pass"); //customerPass��CustomerMail
			
	    	//SDK���Ăяo��
			Intent b = new Intent(MainActivity.this, jp.alij.paydroid.activities.PaymentActivity.class);		
			b.putExtra(Consts.TRANSACTION_INDENT, tr);
			startActivity(b);
    		
    		break;
    	   		
    	case R.id.quick_charge_second:
    		
    		//�K�{�p�����[�^
    		tr.setAmount(210);
    		tr.setSiteId("your_site_id");
    		tr.setSitePass("your_site_password");    		
    		
    		//�_��ɍ��킹��, id��pass(�������̓��[��)��n��
    		tr.setCustomerId("your_customer_id");
    		tr.setCustomerPass("your_customer_pass");
    		
    		//�N�C�b�N�`���[�W�t���b�O
    		tr.setQuickChargeStatus(QuickChargeStatus.QUICK_CHARGE_SECOND_TIME_AND_MORE);
    		
        	//SDK���Ăяo��
    		Intent n = new Intent(MainActivity.this, jp.alij.paydroid.activities.PaymentActivity.class);		
    		n.putExtra(Consts.TRANSACTION_INDENT, tr);
    		startActivity(n);
    		
    		break;
    		
    	case R.id.quick_charge_change:
    		
    		tr.setSiteId("your_site_id");
    		tr.setSitePass("your_site_password"); 
    		
    		tr.setCustomerId("your_customer_id");
    		tr.setCustomerPass("your_customer_pass");
    		
    		tr.setCardName("card_name");
    		tr.setCardNo("xxxxxxxxxxxxxxxx");
    		tr.setCardMonth("xx");
    		tr.setCardYear("xx");
    		
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
