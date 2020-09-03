package com.example.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.criminalintent.R;
import com.example.criminalintent.animation.ZoomOutPageTransformer;
import com.example.criminalintent.controller.fragment.CrimeDetailFragment;
import com.example.criminalintent.model.Crime;
import com.example.criminalintent.repository.CrimeRepository;
import com.example.criminalintent.repository.IRepository;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    public static final String EXTRA_CRIME_ID = "com.example.criminalintent.crimeId";
    public static final String TAG = "CPA";
    private IRepository mRepository;
    private UUID mCrimeId;

    private ViewPager2 mViewPagerCrimes;
    private Button mButtonFirst;
    private Button mButtonPrevious;
    private Button mButtonNext;
    private Button mButtonLast;


    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mRepository = CrimeRepository.getInstance();
        mCrimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        findViews();
        initViews();
        setListener();
    }

    private void setListener() {

        mButtonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPagerCrimes.setCurrentItem(0);

            }
        });
        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextItem=mViewPagerCrimes.getCurrentItem()-1;
                if (nextItem>0)
                    mViewPagerCrimes.setCurrentItem(nextItem);
                else
                    mViewPagerCrimes.setCurrentItem(mRepository.getCrimes().size()-1);

            }
        });
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextItem=mViewPagerCrimes.getCurrentItem()+1;
                if (nextItem<mRepository.getCrimes().size())
                mViewPagerCrimes.setCurrentItem(nextItem);
                else
                    mViewPagerCrimes.setCurrentItem(0);

            }
        });
        mButtonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPagerCrimes.setCurrentItem(mRepository.getCrimes().size()-1);


            }
        });
    }

    private void findViews() {
        mViewPagerCrimes = findViewById(R.id.view_pager_crimes);
        mButtonFirst=findViewById(R.id.btn_first);
        mButtonPrevious=findViewById(R.id.btn_previous);
        mButtonNext=findViewById(R.id.btn_next);
        mButtonLast=findViewById(R.id.btn_last);
    }

    private void initViews() {
        List<Crime> crimes = mRepository.getCrimes();
        CrimePagerAdapter adapter = new CrimePagerAdapter(this, crimes);
        mViewPagerCrimes.setAdapter(adapter);

        int currentIndex = mRepository.getPosition(mCrimeId);

        mViewPagerCrimes.setCurrentItem(currentIndex);
        //zoom animation
        mViewPagerCrimes.setPageTransformer( new ZoomOutPageTransformer());
        //Depth page transformer
        // mViewPagerCrimes.setTranslationX(-1 * mViewPagerCrimes.getWidth() * currentIndex);
        // Depth Out animation
       // mViewPagerCrimes.setPageTransformer(new CubeOutDepthTransformation());
    }


    private class CrimePagerAdapter extends FragmentStateAdapter {

        private List<Crime> mCrimes;

        public List<Crime> getCrimes() {
            return mCrimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public CrimePagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Crime> crimes) {
            super(fragmentActivity);
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Log.d(TAG, "position: " + (position + 1));

            Crime crime = mCrimes.get(position);
            CrimeDetailFragment crimeDetailFragment =
                    CrimeDetailFragment.newInstance(crime.getId());

            return crimeDetailFragment;
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}