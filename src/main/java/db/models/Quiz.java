package db.models;

import org.springframework.lang.Nullable;

public class Quiz {
	@Nullable
	private int id;
	
	private String title;
	
	private String description;
	
	@Nullable
	private String quiz_data;
	
	private double points_possible;
	
	private int context_id;
	
	private String context_type;
	
	private int assignment_id;
	
	private String workflow_state;
	
	private int shuffle_answers;
	
	private boolean show_correct_answers;
	
	@Nullable
	private int time_limit; 
	
	private int allowed_attempts;
	
	private String scoring_policy;
	
}
