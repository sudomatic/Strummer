package net.gulluman.strummer.strummer;

public class StrumPattern {
	private String StrumImageFile;
	private String StrumAudioFile;

	public void setStrumImageFile(String filename)
	{
		StrumImageFile = filename;
	}
	public String getStrumImageFile()
	{
		return StrumImageFile;
	}
	public void setStrumAudioFile(String filename)
	{
		StrumAudioFile = filename;
	}
	public String getStrumAudioFile()
	{
		return StrumAudioFile;
	}
}
